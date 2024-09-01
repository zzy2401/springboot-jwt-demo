# 适合用来练手和参考的一个简单springboot+jwt的实现

---
title: jwt简单的使用
createTime: 2024/08/29 15:53:46
permalink: /article/63l53wha/

---

### jwt实现方法

```java
@Component
public class JwtUtil {
    private static final String secret = "waziwaziwaziwaziwaziwaziwaziwaziwaziwaziwaziwaziwaziwaziwaziwazi";
    private long expire;
    private String header;

    // 生成jwt token
    public static String generateTokenByNames(String username) {
        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + 1000 * 60 * 60 * 24 * 7);

        Map<String, Object> headerClaims = new HashMap<>();
        headerClaims.put("alg", "HS256");
        headerClaims.put("typ", "JWT");

        return JWT.create()
                .withHeader(headerClaims)
                .withClaim("username", username)
                .withIssuedAt(now)
                .withExpiresAt(expiresAt)
                .sign(Algorithm.HMAC512(secret));
    }

    // 获取jwt的信息
    public Map<String, Claim> getClaimByToken(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaims();
    }

    // 获取token的过期时间
    public Date getExpiration(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getExpiresAt();
    }

    // token是否过期
    public boolean isTokenExpired(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getExpiresAt().before(new Date());
    }

    // 验证token
    public boolean verifyToken(String token) {
        Algorithm algorithm = Algorithm.HMAC512(secret);
        JWTVerifier verifier = JWT.require(algorithm).build();
        verifier.verify(token);
        return true;
    }

}

```

### 定义一个响应实体

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderMethodName = "builder")
public class Results {
    private boolean success;
    private int code;
    private String msg;
    private Map<String,Object> data = new HashMap<>();

    public static Results success() {
        return Results.builder()
                .success(true)
                .code(200)
                .msg("success")
                .build();
    }

    public static Results fail() {
        return Results.builder()
                .success(false)
                .code(500)
                .msg("fail")
                .build();
    }

    public Results Data(String key, Object value) {
        if (this.data == null) {
            this.data = new HashMap<>();
        }
        this.data.put(key, value);
        return this;
    }
}
```

### 定义一个User实体

```java
@Data
public class User {
    private int id;
    private String username;
    private String password;
}
```

### 写一个控制器进行测试

```java
@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @PostMapping("/login")
    public Results login(@RequestBody User user) {
        String token = JwtUtil.generateTokenByNames(user.getUsername());
        return Results.success().Data("token", token);
    }

    @GetMapping("/info")
    public Results info(String token) {
        String username = JWT.decode(token).getSubject();
        return Results.success().Data("username", username);
    }
}
```

### pom配置信息：


```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <artifactId>spring-boot-dependencies</artifactId>
        <groupId>org.springframework.boot</groupId>
        <version>3.3.3</version>
    </parent>


    <groupId>com.zzy</groupId>
    <artifactId>back</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>22</maven.compiler.source>
        <maven.compiler.target>22</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <!-- spring-boot-dependencies -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>

        <!-- https://mvnrepository.com/artifact/com.auth0/java-jwt -->
        <dependency>
            <groupId>com.auth0</groupId>
            <artifactId>java-jwt</artifactId>
            <version>4.4.0</version>
        </dependency>

    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <compilerArgs>
                        <arg>-parameters</arg>
                    </compilerArgs>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>
```

