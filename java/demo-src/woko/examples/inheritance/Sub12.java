package woko.examples.inheritance;

import javax.persistence.Entity;

@Entity
public class Sub12 extends Sub1 {

    private String name12;

    public String getName12() {
        return name12;
    }

    public void setName12(String name12) {
        this.name12 = name12;
    }

}
