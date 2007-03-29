package woko.examples.inheritance;

import javax.persistence.Entity;

@Entity
public abstract class Sub1 extends BaseObject {

    private String name1;

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }


}