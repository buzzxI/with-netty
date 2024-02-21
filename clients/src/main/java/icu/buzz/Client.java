package icu.buzz;

import icu.buzz.rpc.ServiceFactory;

public class Client {
    public static void main(String[] args) {
        // rpc client
        Service service = ServiceFactory.getService(Service.class);
        System.out.println(service.echo("hello"));
        System.out.println(service.echo("buzz"));
    }
}