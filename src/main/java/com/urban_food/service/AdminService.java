package com.urban_food.service;

import com.urban_food.entity.Admin;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

@Service
public class AdminService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcCall createAdminCall;
    private SimpleJdbcCall getAllAdminsCall;
    private SimpleJdbcCall getAdminByIdCall;
    private SimpleJdbcCall updateAdminCall;
    private SimpleJdbcCall deleteAdminCall;
    private SimpleJdbcCall loginAdminCall;

    @PostConstruct
    public void init() {
        createAdminCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("CREATE_ADMIN_PROC");
        getAllAdminsCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("GET_ALL_ADMINS_PROC")
                .returningResultSet("o_cursor", (rs, rowNum) -> mapAdmin(rs));
        getAdminByIdCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("GET_ADMIN_BY_ID_PROC")
                .declareParameters(new SqlParameter("p_adminID", Types.VARCHAR))
                .returningResultSet("o_cursor", (rs, rowNum) -> mapAdmin(rs));
        updateAdminCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("UPDATE_ADMIN_PROC");
        deleteAdminCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("DELETE_ADMIN_PROC");
        loginAdminCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("LOGIN_ADMIN_PROC")
                .declareParameters(
                        new SqlParameter("p_username", Types.VARCHAR),
                        new SqlParameter("p_password", Types.VARCHAR),
                        new SqlOutParameter("p_result", Types.VARCHAR)
                );
    }

    private Admin mapAdmin(ResultSet rs) throws SQLException {
        Admin admin = new Admin();
        admin.setAdminID(rs.getString("adminID"));
        admin.setAdminEmail(rs.getString("adminEmail"));
        admin.setAdminUsername(rs.getString("adminUsername"));
        admin.setAdminAddress(rs.getString("adminAddress"));
        admin.setAdminContact(rs.getString("adminContact"));
        admin.setAdminPassword(rs.getString("adminPassword"));
        return admin;
    }

    public void createAdmin(Admin admin) {
        Map<String, Object> params = new HashMap<>();
        params.put("p_adminID", admin.getAdminID());
        params.put("p_adminEmail", admin.getAdminEmail());
        params.put("p_adminUsername", admin.getAdminUsername());
        params.put("p_adminAddress", admin.getAdminAddress());
        params.put("p_adminContact", admin.getAdminContact());
        params.put("p_adminPassword", admin.getAdminPassword());
        createAdminCall.execute(params);
    }

    public List<Admin> getAllAdmins() {
        Map<String, Object> result = getAllAdminsCall.execute(new HashMap<>());
        return (List<Admin>) result.get("o_cursor");
    }

    public Optional<Admin> getAdminById(String adminID) {
        Map<String, Object> result = getAdminByIdCall.execute(Collections.singletonMap("p_adminID", adminID));
        List<Admin> list = (List<Admin>) result.get("o_cursor");
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    public void updateAdmin(Admin admin) {
        Map<String, Object> params = new HashMap<>();
        params.put("p_adminID", admin.getAdminID());
        params.put("p_adminEmail", admin.getAdminEmail());
        params.put("p_adminUsername", admin.getAdminUsername());
        params.put("p_adminAddress", admin.getAdminAddress());
        params.put("p_adminContact", admin.getAdminContact());
        params.put("p_adminPassword", admin.getAdminPassword());
        updateAdminCall.execute(params);
    }

    public void deleteAdmin(String adminID) {
        deleteAdminCall.execute(Collections.singletonMap("p_adminID", adminID));
    }

    public String loginAdmin(String username, String password) {
        Map<String, Object> result = loginAdminCall.execute(Map.of(
                "p_username", username,
                "p_password", password
        ));
        return (String) result.get("p_result");
    }
}
