package service;

import model.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class UserService {
    private static UserService instance;

    /* хранилище данных */
    private Map<Long, User> dataBase;
    /* счетчик id пользователей */
    private AtomicLong maxId;
    /* хранилище залогиненых пользователей */
    private Map<Long, User> authMap;

    private UserService() {
     this.dataBase = Collections.synchronizedMap(new HashMap<>());
     this.maxId = new AtomicLong(0);
     this.authMap = Collections.synchronizedMap(new HashMap<>());
    }

    public static UserService getInstance() {
        if (instance == null) {        //если объект еще не создан
            instance = new UserService();    //создать новый объект
        }
        return instance;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(dataBase.values());
    }

    public User getUserById(Long id) {
        return dataBase.get(id);
    }

    public boolean addUser(User user) {
        if (isExistsThisUser(user)) {
            return false;
        } else {
            dataBase.put(maxId.longValue(), user);
            user.setId(maxId.longValue());
            maxId.incrementAndGet();
            return true;
        }
    }

    public void deleteAllUser() {
        logoutAllUsers();
        dataBase.clear();
        maxId = new AtomicLong(0);
    }

    public boolean isExistsThisUser(User user) {
        for (Map.Entry<Long, User> entry : dataBase.entrySet()) {
            User value = entry.getValue();
            if ((value.getEmail().equals(user.getEmail())) && (value.getPassword().equals(user.getPassword()))) {
                return true;
            }
        }
        return false;
    }

    public boolean isExistsEmailUser (String email) {
        if (dataBase.isEmpty()) return false;
        for (Map.Entry<Long, User> entry : dataBase.entrySet()) {
            User value = entry.getValue();
            if (value.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    public List<User> getAllAuth() {
        return  new ArrayList<>(authMap.values());
    }

    public boolean authUser(User user) {
        long id = 0;
        for (Map.Entry<Long, User> entry : dataBase.entrySet()) {
            long key = entry.getKey();
            User value = entry.getValue();
            if ((value.getEmail().equals(user.getEmail()))&&(value.getPassword().equals(user.getPassword()))) {
                if (isLoginThisUser(user)) {
                    return false;
                } else {
                    id = key;
                    authMap.put(id, user);
                    user.setId(id);
                    return true;
                }
            }
        }
        return false;
    }

    public void logoutAllUsers() {
        authMap.clear();
    }

    public boolean isUserAuthById(Long id) {
        return authMap.containsKey(id);
    }

    public boolean isLoginThisUser(User user) {
       for (Map.Entry<Long, User> entry : authMap.entrySet()) {
            User value = entry.getValue();
            if (value.getEmail().equals(user.getEmail())) {
                return true;
            }
        }
        return false;
    }
}
