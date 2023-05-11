package com.luxrest.gui.Controllers.Dashboard;

public class DashboardController {

    private static DashboardController instance;

    public DashboardController(){
        instance = this;
    }

    public static DashboardController getInstance(){
        return instance;
    }

}
