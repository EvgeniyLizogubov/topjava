package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Repository
public class JdbcUserRepository implements UserRepository {
    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else {
            int updateResult = namedParameterJdbcTemplate.update("""
                    UPDATE users SET name=:name, email=:email, password=:password, 
                    registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource);

            if (updateResult != 0) {
                namedParameterJdbcTemplate.update("DELETE FROM user_role WHERE user_id=:id", parameterSource);
            } else {
                return null;
            }
        }
        jdbcTemplate.batchUpdate(
                "INSERT INTO user_role(user_id, roles) VALUES (?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        preparedStatement.setInt(1, user.getId());
                        preparedStatement.setString(2, user.getRoles().toArray()[i].toString());
                    }

                    @Override
                    public int getBatchSize() {
                        return user.getRoles().size();
                    }
        });
        return user;
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query(
                "SELECT users.*, user_role.roles FROM users JOIN user_role ON user_role.user_id=users.id WHERE users.id=?",
                new ResultSetExtractor()::extractData,
                id
        );
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query(
                "SELECT users.*, user_role.roles FROM users JOIN user_role ON user_role.user_id=users.id WHERE email=?",
                new ResultSetExtractor()::extractData,
                email
        );
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(
                "SELECT * FROM users LEFT JOIN user_role ON user_role.user_id=users.id ORDER BY name, email",
                new ResultSetExtractor()::extractData
        );
    }

    public class ResultSetExtractor {
        public List<User> extractData(ResultSet rs) throws SQLException {
            List<User> users = new ArrayList<>();
            rs.next();
            while(!rs.isAfterLast() && rs.getRow() != 0) {
                Set<Role> roles = new LinkedHashSet<>();
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setEnabled(rs.getBoolean("enabled"));
                user.setRegistered(rs.getTimestamp("registered"));
                user.setCaloriesPerDay(rs.getInt("calories_per_day"));
                do {
                    String role = rs.getString("roles");
                    if (role != null) {
                        roles.add(Role.valueOf(role));
                    }
                } while (rs.next() && rs.getInt("id") == user.getId());
                user.setRoles(roles);
                users.add(user);
            }
            return users;
        }
    }
}
