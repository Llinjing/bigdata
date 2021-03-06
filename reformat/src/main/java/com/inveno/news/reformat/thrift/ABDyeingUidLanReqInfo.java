package com.inveno.news.reformat.thrift;
/**
 * Autogenerated by Thrift Compiler (0.9.3)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Collections;
import javax.annotation.Generated;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked"})
@Generated(value = "Autogenerated by Thrift Compiler (0.9.3)", date = "2017-03-23")
public class ABDyeingUidLanReqInfo implements org.apache.thrift.TBase<ABDyeingUidLanReqInfo, ABDyeingUidLanReqInfo._Fields>, java.io.Serializable, Cloneable, Comparable<ABDyeingUidLanReqInfo> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("ABDyeingUidLanReqInfo");

  private static final org.apache.thrift.protocol.TField UID_FIELD_DESC = new org.apache.thrift.protocol.TField("uid", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField _UID_LAN_INFO_FIELD_DESC = new org.apache.thrift.protocol.TField("_uid_lan_info", org.apache.thrift.protocol.TType.STRUCT, (short)2);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new ABDyeingUidLanReqInfoStandardSchemeFactory());
    schemes.put(TupleScheme.class, new ABDyeingUidLanReqInfoTupleSchemeFactory());
  }

  public String uid; // required
  public ABDyeingLanRequest _uid_lan_info; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    UID((short)1, "uid"),
    _UID_LAN_INFO((short)2, "_uid_lan_info");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // UID
          return UID;
        case 2: // _UID_LAN_INFO
          return _UID_LAN_INFO;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.UID, new org.apache.thrift.meta_data.FieldMetaData("uid", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields._UID_LAN_INFO, new org.apache.thrift.meta_data.FieldMetaData("_uid_lan_info", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, ABDyeingLanRequest.class)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(ABDyeingUidLanReqInfo.class, metaDataMap);
  }

  public ABDyeingUidLanReqInfo() {
  }

  public ABDyeingUidLanReqInfo(
    String uid,
    ABDyeingLanRequest _uid_lan_info)
  {
    this();
    this.uid = uid;
    this._uid_lan_info = _uid_lan_info;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public ABDyeingUidLanReqInfo(ABDyeingUidLanReqInfo other) {
    if (other.isSetUid()) {
      this.uid = other.uid;
    }
    if (other.isSet_uid_lan_info()) {
      this._uid_lan_info = new ABDyeingLanRequest(other._uid_lan_info);
    }
  }

  public ABDyeingUidLanReqInfo deepCopy() {
    return new ABDyeingUidLanReqInfo(this);
  }

  @Override
  public void clear() {
    this.uid = null;
    this._uid_lan_info = null;
  }

  public String getUid() {
    return this.uid;
  }

  public ABDyeingUidLanReqInfo setUid(String uid) {
    this.uid = uid;
    return this;
  }

  public void unsetUid() {
    this.uid = null;
  }

  /** Returns true if field uid is set (has been assigned a value) and false otherwise */
  public boolean isSetUid() {
    return this.uid != null;
  }

  public void setUidIsSet(boolean value) {
    if (!value) {
      this.uid = null;
    }
  }

  public ABDyeingLanRequest get_uid_lan_info() {
    return this._uid_lan_info;
  }

  public ABDyeingUidLanReqInfo set_uid_lan_info(ABDyeingLanRequest _uid_lan_info) {
    this._uid_lan_info = _uid_lan_info;
    return this;
  }

  public void unset_uid_lan_info() {
    this._uid_lan_info = null;
  }

  /** Returns true if field _uid_lan_info is set (has been assigned a value) and false otherwise */
  public boolean isSet_uid_lan_info() {
    return this._uid_lan_info != null;
  }

  public void set_uid_lan_infoIsSet(boolean value) {
    if (!value) {
      this._uid_lan_info = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case UID:
      if (value == null) {
        unsetUid();
      } else {
        setUid((String)value);
      }
      break;

    case _UID_LAN_INFO:
      if (value == null) {
        unset_uid_lan_info();
      } else {
        set_uid_lan_info((ABDyeingLanRequest)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case UID:
      return getUid();

    case _UID_LAN_INFO:
      return get_uid_lan_info();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case UID:
      return isSetUid();
    case _UID_LAN_INFO:
      return isSet_uid_lan_info();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof ABDyeingUidLanReqInfo)
      return this.equals((ABDyeingUidLanReqInfo)that);
    return false;
  }

  public boolean equals(ABDyeingUidLanReqInfo that) {
    if (that == null)
      return false;

    boolean this_present_uid = true && this.isSetUid();
    boolean that_present_uid = true && that.isSetUid();
    if (this_present_uid || that_present_uid) {
      if (!(this_present_uid && that_present_uid))
        return false;
      if (!this.uid.equals(that.uid))
        return false;
    }

    boolean this_present__uid_lan_info = true && this.isSet_uid_lan_info();
    boolean that_present__uid_lan_info = true && that.isSet_uid_lan_info();
    if (this_present__uid_lan_info || that_present__uid_lan_info) {
      if (!(this_present__uid_lan_info && that_present__uid_lan_info))
        return false;
      if (!this._uid_lan_info.equals(that._uid_lan_info))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_uid = true && (isSetUid());
    list.add(present_uid);
    if (present_uid)
      list.add(uid);

    boolean present__uid_lan_info = true && (isSet_uid_lan_info());
    list.add(present__uid_lan_info);
    if (present__uid_lan_info)
      list.add(_uid_lan_info);

    return list.hashCode();
  }

  @Override
  public int compareTo(ABDyeingUidLanReqInfo other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetUid()).compareTo(other.isSetUid());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetUid()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.uid, other.uid);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSet_uid_lan_info()).compareTo(other.isSet_uid_lan_info());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSet_uid_lan_info()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this._uid_lan_info, other._uid_lan_info);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("ABDyeingUidLanReqInfo(");
    boolean first = true;

    sb.append("uid:");
    if (this.uid == null) {
      sb.append("null");
    } else {
      sb.append(this.uid);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("_uid_lan_info:");
    if (this._uid_lan_info == null) {
      sb.append("null");
    } else {
      sb.append(this._uid_lan_info);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (uid == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'uid' was not present! Struct: " + toString());
    }
    if (_uid_lan_info == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field '_uid_lan_info' was not present! Struct: " + toString());
    }
    // check for sub-struct validity
    if (_uid_lan_info != null) {
      _uid_lan_info.validate();
    }
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class ABDyeingUidLanReqInfoStandardSchemeFactory implements SchemeFactory {
    public ABDyeingUidLanReqInfoStandardScheme getScheme() {
      return new ABDyeingUidLanReqInfoStandardScheme();
    }
  }

  private static class ABDyeingUidLanReqInfoStandardScheme extends StandardScheme<ABDyeingUidLanReqInfo> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, ABDyeingUidLanReqInfo struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // UID
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.uid = iprot.readString();
              struct.setUidIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // _UID_LAN_INFO
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct._uid_lan_info = new ABDyeingLanRequest();
              struct._uid_lan_info.read(iprot);
              struct.set_uid_lan_infoIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, ABDyeingUidLanReqInfo struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.uid != null) {
        oprot.writeFieldBegin(UID_FIELD_DESC);
        oprot.writeString(struct.uid);
        oprot.writeFieldEnd();
      }
      if (struct._uid_lan_info != null) {
        oprot.writeFieldBegin(_UID_LAN_INFO_FIELD_DESC);
        struct._uid_lan_info.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class ABDyeingUidLanReqInfoTupleSchemeFactory implements SchemeFactory {
    public ABDyeingUidLanReqInfoTupleScheme getScheme() {
      return new ABDyeingUidLanReqInfoTupleScheme();
    }
  }

  private static class ABDyeingUidLanReqInfoTupleScheme extends TupleScheme<ABDyeingUidLanReqInfo> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, ABDyeingUidLanReqInfo struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      oprot.writeString(struct.uid);
      struct._uid_lan_info.write(oprot);
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, ABDyeingUidLanReqInfo struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      struct.uid = iprot.readString();
      struct.setUidIsSet(true);
      struct._uid_lan_info = new ABDyeingLanRequest();
      struct._uid_lan_info.read(iprot);
      struct.set_uid_lan_infoIsSet(true);
    }
  }

}

