package service.impl;

import dao.AdminDao;
import domain.Admin;
import domain.PageBean;
import service.AdminService;

import java.util.List;

public class AdminServiceImpl implements AdminService{

    private AdminDao adminDao;

    public void setAdminDao(AdminDao adminDao) {
        this.adminDao = adminDao;
    }

    @Override
    public Admin getAdminByUserName(Admin admin) {
        return adminDao.getAdminByUserName(admin);
    }

    @Override
    public Admin updateAdminInfo(Admin admin) {
        return adminDao.updateAdminInfo(admin);
    }

    @Override
    public List<Admin> getAllAdmins() {
        return adminDao.getAllAdmins();
    }

    @Override
    public boolean addAdmin(Admin admin) {
        return adminDao.addAdmin(admin);
    }

    @Override
    public Admin getAdminById(Admin admin) {
        return adminDao.getAdminById(admin);
    }

    @Override
    public PageBean<Admin> findAdminByPage(int pageCode, int pageSize) {
        return adminDao.findAdminByPage(pageCode, pageSize);
    }

    @Override
    public PageBean<Admin> queryAdmin(Admin admin, int pageCode, int pageSize) {
        return adminDao.queryAdmin(admin, pageCode, pageSize);
    }

    @Override
    public boolean deleteAdmin(Admin admin) {
        return adminDao.deleteAdmin(admin);
    }
}
