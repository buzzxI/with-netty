package icu.buzz;

import icu.buzz.pojo.User;

public interface Service {
    User getUser(String name);

    String echo(String msg);
}
