package icu.buzz;

import icu.buzz.rpc.ServiceFactory;

public class Client {
    public static void main(String[] args) {
        // rpc client
        Service service = ServiceFactory.getService(Service.class);
        String msg = service.echo("hello");
        System.out.println(msg);
    }
}