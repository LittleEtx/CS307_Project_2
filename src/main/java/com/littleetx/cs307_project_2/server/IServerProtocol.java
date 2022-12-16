package com.littleetx.cs307_project_2.server;

import cs307.project2.interfaces.StaffInfo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IServerProtocol extends Remote {
    int verify(String username, String password) throws RemoteException;

    StaffInfo getStaffInfo(int id) throws RemoteException;

    //courier methods


}
