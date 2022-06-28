# 一些常用 JPA 注解的体验

## @Entity 和 @Table

> 此小节目的在于对比这两个注解的用法
---

### 使用注解 @Entity

仅使用注解 @Entity 且使用了虚假的类名

```java

@Data
@Entity
public class FakeBox {

    @Id
    private String id;

    private String palletId;

}
```

> select fakebox0_.id as id1_0_0_, fakebox0_.pallet_id as pallet_i2_0_0_ from **fake_box** fakebox0_ where fakebox0_
> .id=?

组装出的 SQL 使用类名作为表名

---
使用注解 @Entity 和虚假的类名，并显式声明表名

```java

@Data
@Entity(name = "box")
public class FakeBox {

}
```

> select fakebox0_.id as id1_0_0_, fakebox0_.pallet_id as pallet_i2_0_0_ from **box** fakebox0_ where fakebox0_.id=?

可以组装出正确的 SQL

---

### 使用注解 @Table

仅使用注解 @Table 不使用 @Entity，并且使用虚假的类名

```java

@Data
@Table
public class FakeBox {

}
```

> Not a managed type: class xyz.hapilemon.jpa.box.FakeBox

启动时报错

---
使用注解 @Table，且使用正确类名

```java

@Data
@Table
public class Box {

}
```

> Not a managed type: class xyz.hapilemon.jpa.box.Box

启动时报错

---

仅使用注解 @Table 并显式声明表名，但使用虚假类名

```java

@Data
@Table(name = "box")
public class FakeBox {

}
```

> Not a managed type: class xyz.hapilemon.jpa.box.FakeBox

启动时报错

---

### 使用 @Entity 和 @Table 注解

使用 @Entity 和 @Table 注解，并且在 @Table 中显式声明表名

```java

@Data
@Entity
@Table(name = "box")
public class FakeBox {

}
```

> select fakebox0_.id as id1_0_0_, fakebox0_.pallet_id as pallet_i2_0_0_ from **box** fakebox0_ where fakebox0_.id=?

可以正确组装出 SQL

---
使用 @Entity 和 @Table 注解，并且分别显示声明正确的表名和错误的表名

```java

@Data
@Entity(name = "fakeBox")
@Table(name = "box")
public class FakeBox {

}
```

> select fakebox0_.id as id1_0_0_, fakebox0_.pallet_id as pallet_i2_0_0_ from **box** fakebox0_ where fakebox0_.id=?

可以组装出正确的 SQL

---
使用 @Entity 和 @Table 注解，并且分别显示声明正确的表名和错误的表名

```java

@Data
@Entity(name = "box")
@Table(name = "fakeBox")
public class FakeBox {

}
```

> select fakebox0_.id as id1_0_0_, fakebox0_.pallet_id as pallet_i2_0_0_ from **fake_box** fakebox0_ where fakebox0_
> .id=?

组装出了错误的 SQL

### 小结

一般情况下只需要且**必须**使用注解 @Entity，若类名与表名一致则不要显示声明；反之需要显示声明

@Table 注解更多是为了更清晰地描述这张表时使用

当同时使用了 @Entity 和 @Table 时，会优先使用在 @Table 中声明的表名

## @JoinColumn

> 此小节的目的在于摸清楚 @JoinColumn 的 name 属性应该传什么值
---

```java

@Data
@Entity
public class Pallet {

    @Id
    private String id;

    private String warehouse;

    @JsonIgnore
    @OneToMany
    @JoinColumn(name = "palletId", referencedColumnName = "id")
    private List<Box> boxList;
}
```

```java

@Data
@Entity
public class Box {

    @Id
    private String id;

    private String palletId;

    @ManyToOne
    @JoinColumn(name = "palletId", insertable = false, updatable = false)
    private Pallet pallet;

}
```

正确的写法

---

```java

@Data
@Entity
public class Box {

    @Id
    private String id;

    private String palletId;

    @ManyToOne
    @JoinColumn(name = "palletId")
    private Pallet pallet;

}
```

```java

@Data
@Entity
public class Pallet {

    @Id
    private String id;

    private String warehouse;

    @OneToMany
    @JoinColumn(name = "id", referencedColumnName = "palletId")
    private List<Box> boxList;
}
```

> Unable to map collection xyz.hapilemon.jpa.pallet.Pallet.boxList
>
> Unable to find column with logical name: palletId in org.hibernate.mapping.Table(pallet) and its related supertables
> and secondary tables

报错的原因是 referencedColumnName 和 name 的值写反了，此时 name 应该传 Box 表的 palletId、referencedColumnName 应该传Pallet 表的 id

---

```java

@Data
@Entity
public class Box {

    @Id
    private String id;

    private String palletId;

    @ManyToOne
    @JoinColumn(name = "palletId")
    private Pallet pallet;

}
```

```java

@Data
@Entity
public class Pallet {

    @Id
    private String id;

    private String warehouse;

    @OneToMany
    @JoinColumn(name = "palletId", referencedColumnName = "id")
    private List<Box> boxList;
}
```

> Repeated column in mapping for entity: xyz.hapilemon.jpa.box.Box column: pallet_id (should be mapped with insert="
> false" update="false")

报错的原因是 Box 类中存在 palletId 和 Pallet.id，两者会产生冲突。@Column#name 和 @JoinColumn#name 指向的是同一个列，两个值可能会产生冲突，因此需要忽略掉其中一个

---

```java

@Data
@Entity
public class Pallet {

    @Id
    private String id;

    private String warehouse;

    @JsonIgnore
    @OneToMany
    @JoinColumn(name = "fakePalletIdInPallet", referencedColumnName = "id")
    private List<Box> boxList;
}
```

```java

@Data
@Entity
public class Box {

    @Id
    private String id;

    private String palletId;

    @ManyToOne
    @JoinColumn(name = "fakePalletIdInBox", insertable = false, updatable = false)
    private Pallet pallet;

}
```

> select box0_.id as id1_0_0_, box0_.fake_pallet_id_in_box as fake_pal3_0_0_, box0_.pallet_id as pallet_i2_0_0_,
> pallet1_.id as id1_1_1_, pallet1_.warehouse as warehous2_1_1_ from box box0_ left outer join pallet pallet1_ on box0_.**
> fake_pallet_id_in_box**=pallet1_.id where box0_.id=?

> select boxlist0_.**fake_pallet_id_in_pallet** as fake_pal4_0_0_, boxlist0_.id as id1_0_0_, boxlist0_.id as id1_0_1_,
> boxlist0_.**fake_pallet_id_in_box** as fake_pal3_0_1_, boxlist0_.pallet_id as pallet_i2_0_1_, pallet1_.id as id1_1_2_,
> pallet1_.warehouse as warehous2_1_2_ from box boxlist0_ left outer join pallet pallet1_ on boxlist0_.**
> fake_pallet_id_in_box**=pallet1_.id where boxlist0_.**fake_pallet_id_in_pallet**=?

@JoinColumn#name 决定了组装 SQL 的时候的列名

---

```java

@Data
@Entity
public class Box {

    @Id
    private String id;

    @Column(name = "fake_pallet_id")
    private String palletId;

    @ManyToOne
    @JoinColumn(name = "palletId", insertable = false, updatable = false)
    private Pallet pallet;

}
```

> select box0_.id as id1_0_0_, box0_.pallet_id as pallet_i3_0_0_, box0_.**fake_pallet_id** as fake_pal2_0_0_, pallet1_
> .id as id1_1_1_, pallet1_.warehouse as warehous2_1_1_ from box box0_ left outer join pallet pallet1_ on box0_.**
> pallet_id**=pallet1_.id where box0_.id=?

* [ ] TODO 命名策略。关键词 physical-strategy

---

```java

@Data
@Entity
public class Pallet {

    @Id
    private String id;

    private String warehouse;

    @JsonIgnore
    @OneToMany
    @JoinColumn(name = "palletId", referencedColumnName = "id")
    private List<Box> boxList;
}
```

```java

@Data
@Entity
public class Box {

    @Id
    private String id;

    @Column(name = "pallet_id")
    private String notPalletId;

    @ManyToOne
    @JoinColumn(name = "notPalletId", insertable = false, updatable = false)
    private Pallet pallet;

}
```

> Table [box] contains physical column name [pallet_id] referred to by multiple logical column names: [palletId]
> , [pallet_id]

因为相同的列 Box 类给他的逻辑列名是 pallet_id，Pallet 类给他的逻辑列名是 palletId，所以报错。两者统一即可

---

```java

@Data
@Entity
public class Box {

    @Id
    private String id;

    @Column(name = "pallet_id")
    private String notPalletId;

    @ManyToOne
    @JoinColumn(name = "notPalletId", insertable = false, updatable = false)
    private Pallet pallet;

}
```

```java

@Data
@Entity
public class Pallet {

    @Id
    private String id;

    private String warehouse;

    @JsonIgnore
    @OneToMany
    @JoinColumn(name = "pallet_id", referencedColumnName = "id")
    private List<Box> boxList;
}
```

> select box0_.id as id1_0_0_, box0_.**pallet_id** as pallet_i2_0_0_, box0_.**not_pallet_id** as not_pall3_0_0_,
> pallet1_.id as id1_1_1_, pallet1_.warehouse as warehous2_1_1_ from box box0_ left outer join pallet pallet1_ on box0_.**
> not_pallet_id**=pallet1_.id where box0_.id=?

### 小结
@JoinColumn 注解使用时，name 属性传“一”中代表“多”的列的列名，referencedColumnName 属性传“多”中被“一”关联的列的列名