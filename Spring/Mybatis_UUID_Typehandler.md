# Mybatis - UuidTypeHandler

Postgresql을 사용하면서 type중에 UUID를 사용할 일이 있었다.

UUID는 java util package에서 지원해준다.

```java
import java.util.UUID;

public class Order {
    private UUID orderId = UUID.randomUUID();
    private String orderName;
    ...
}
```

그런데 실행하고 java의 UUID를 insert하려고하면 

```
uuid but expression is of type character varying
```

이라는 error 문구가 뜨는데, 알아본 결과 uuid를 mybatis가 매핑해 줄 수 없다(?)는 것이었다.

그래서 TypeHandler를 상속받아 mybatis mapper xml에 handler를 매핑시켜주면 된다.

```java
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.UUID;

/**
 * Handler for UUID types.
 *
 * @see UUID
 */
@MappedTypes({UUID.class})
public class UuidTypeHandler implements TypeHandler<UUID> {
    private static final Logger LOG = LoggerFactory.getLogger(UuidTypeHandler.class);

    @Override
    public void setParameter(PreparedStatement ps, int i, UUID parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null) {
            ps.setObject(i, null, Types.OTHER);
        } else {
            ps.setObject(i, parameter.toString(), Types.OTHER);
        }

    }

    @Override
    public UUID getResult(ResultSet rs, String columnName) throws SQLException {
        return toUUID(rs.getString(columnName));
    }

    @Override
    public UUID getResult(ResultSet rs, int columnIndex) throws SQLException {
        return toUUID(rs.getString(columnIndex));
    }

    @Override
    public UUID getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return toUUID(cs.getString(columnIndex));
    }

    private static UUID toUUID(String val) throws SQLException {
        if (StringUtils.isEmpty(val)) {
            return null;
        }
        try {
            return UUID.fromString(val);
        } catch (IllegalArgumentException e) {
            LOG.warn("Bad UUID found: {}", val);
        }
        return null;
    }
}
```

TypeHandler를 상속받아 구현해주고

```xml
    <insert id="save" parameterType="Order">
        INSERT INTO order_table
        (order_id, order_name, ...)
        VALUES
        (#{orderId, typeHandler=pacakge.UuidTypeHandler}, #{orderName}, ...);
    </insert>
```

다음과 같이 #{orderId, typeHandler =UuidTypeHandler}로 UUID에 대한 TypeHandler를 매핑시켜주면 끝난다.

