package net.sourceforge.jfacets.annotations;

/**
 * Enum that holds possible types of key duplication policies.
 */
public enum DuplicatedKeyPolicyType {

    /**
     * Throw an exception when duplicated keys are found in scanned classes
     */
    ThrowException,

    /**
     * First scanned class wins over the other(s)
     */
    FirstScannedWins

}
