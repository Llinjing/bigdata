/**
 * Autogenerated by Thrift Compiler (0.9.3)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.inveno.bigdata.query.data.util.thrift.ufs;

public enum ActionType implements org.apache.thrift.TEnum {
  TYPE_CLICK(1),
  TYPE_IMPRESSION(2);

  private final int value;

  private ActionType(int value) {
    this.value = value;
  }

  /**
   * Get the integer value of this enum value, as defined in the Thrift IDL.
   */
  public int getValue() {
    return value;
  }

  /**
   * Find a the enum type by its integer value, as defined in the Thrift IDL.
   * @return null if the value is not found.
   */
  public static ActionType findByValue(int value) { 
    switch (value) {
      case 1:
        return TYPE_CLICK;
      case 2:
        return TYPE_IMPRESSION;
      default:
        return null;
    }
  }
}
