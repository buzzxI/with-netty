package icu.buzz;

import icu.buzz.pojo.User;

import java.util.HashMap;
import java.util.Map;

public class ServiceImpl implements Service{

    @Override
    public String echo(String msg) {
        return msg;
    }
}
