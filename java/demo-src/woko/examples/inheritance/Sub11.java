package woko.examples.inheritance;

import javax.persistence.Entity;

/**
 * Created by IntelliJ IDEA.
 * User: vankeisb
 * Date: 8 févr. 2007
 * Time: 20:01:03
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class Sub11 extends Sub1 {

    private String name11;

    public String getName11() {
        return name11;
    }

    public void setName11(String name11) {
        this.name11 = name11;
    }
    
}
