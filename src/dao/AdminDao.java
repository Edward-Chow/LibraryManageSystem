package dao;

import domain.Admin;
import domain.PageBean;

import java.util.List;

public interface AdminDao {

    public Admin getAdminByUserName(Admin admin);

    public Admin updateAdminInfo(Admin admin);

    public List<Admin> getAllAdmins();

    public boolean addAdmin(Admin admin);

    public Admin getAdminById(Admin admin);

    public PageBean<Admin> findAdminByPage(int pageCode, int pageSize);

    public PageBean<Admin> queryAdmin(Admin admin,int pageCode, int pageSize);

    public boolean deleteAdmin(Admin admin);
}
