package com.littleetx.cs307_project_2.server;

import cs307.project2.interfaces.StaffInfo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IServerProtocol extends Remote {
    boolean verify(String username, String password) throws RemoteException;

    StaffInfo getStaffInfo() throws RemoteException;

    int getStaffID() throws RemoteException;

}
