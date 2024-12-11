package com.android.sql.bean;

// User类用于表示应用中的用户实体，通常会包含与用户相关的关键信息，在这个类中简单地定义了用户的ID和用户名这两个属性。
public class User {
    // 用于存储用户的唯一标识符，类型为Integer，在数据库操作或者应用的用户管理体系中，可以通过这个ID来区分不同的用户，进行数据查询、更新等操作。
    Integer id;
    // 用于存储用户的名称（用户名），类型为String，是用户在应用中用于标识自己的一个重要信息，比如登录、显示个人信息等场景会用到。
    String username;
    String password;

    public static User sUser;

    public static User getUser(){return sUser;}

    public static void setUser(User user) {
        sUser = user;
    }

    public User(String username,String password){
        this.username=username;
        this.password=password;
    }


    // 获取用户名的方法，外部类可以通过调用这个方法获取到当前User对象所代表用户的用户名信息。
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }



    // 设置用户名的方法，外部类可以通过调用这个方法来更新当前User对象的用户名属性，传入新的用户名作为参数即可修改。
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    // 获取用户ID的方法，方便外部类获取当前User对象对应的用户唯一标识符，用于在不同的业务逻辑场景中（如关联用户相关的数据等）使用该ID。
    public Integer getId() {
        return id;
    }

    // 设置用户ID的方法，用于更新当前User对象的用户ID属性，不过在实际应用中，用户ID通常是由系统生成且不可随意更改的，
    // 所以这个方法的使用场景可能相对有限，具体要根据业务规则来确定是否调用。
    public void setId(Integer id) {
        this.id = id;
    }

    // 用户类的构造函数，用于创建一个新的User对象实例，需要传入用户的ID和用户名作为参数，
    // 通过这个构造函数可以方便地初始化一个完整的User对象，代表一个具体的用户实体，便于后续在应用中进行操作和管理。
    public User(Integer id, String username) {
        this.id = id;
        this.username = username;
    }
}