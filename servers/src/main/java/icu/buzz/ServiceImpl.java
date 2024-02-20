package icu.buzz;

import icu.buzz.pojo.User;

import java.util.HashMap;
import java.util.Map;

public class ServiceImpl implements Service{
    private Map<String, User> m;

    public ServiceImpl() {
        this.m = new HashMap<>();
    }

    public void addUser(User user) {
        m.put(user.getName(), user);
    }

    @Override
    public User getUser(String name) {
        return m.get(name);
    }

    @Override
    public String echo(String msg) {
        return msg;
    }
}
