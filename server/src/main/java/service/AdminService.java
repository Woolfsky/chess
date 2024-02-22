package service;

import dataAccess.AuthDAO;
import model.AuthData;

public class AdminService {
    /*
    AdminService handles one method: clear
     */

    public static boolean delete() {
        AuthDAO a = new AuthDAO();
        return a.deleteData();
    }



}
