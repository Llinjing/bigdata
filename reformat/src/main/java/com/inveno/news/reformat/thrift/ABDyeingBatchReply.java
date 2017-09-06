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
import java.util.BitSet;
import javax.annotation.Generated;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked"})
@Generated(value = "Autogenerated by Thrift Compiler (0.9.3)", date = "2017-03-23")
public class ABDyeingBatchReply implements org.apache.thrift.TBase<ABDyeingBatchReply, ABDyeingBatchReply._Fields>, java.io.Serializable, Cloneable, Comparable<ABDyeingBatchReply> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("ABDyeingBatchReply");

  private static final org.apache.thrift.protocol.TField STATUS_FIELD_DESC = new org.apache.thrift.protocol.TField("status", org.apache.thrift.protocol.TType.I32, (short)1);
  private static final org.apache.thrift.protocol.TField _UID_REP_INFOS_FIELD_DESC = new org.apache.thrift.protocol.TField("_uid_rep_infos", org.apache.thrift.protocol.TType.LIST, (short)2);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new ABDyeingBatchReplyStandardSchemeFactory());
    schemes.put(TupleScheme.class, new ABDyeingBatchReplyTupleSchemeFactory());
  }

  /**
   * 
   * @see ab_status
   */
  public ab_status status; // required
  public List<ABDyeingUidRepInfo> _uid_rep_infos; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    /**
     * 
     * @see ab_status
     */
    STATUS((short)1, "status"),
    _UID_REP_INFOS((short)2, "_uid_rep_infos");

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
        case 1: // STATUS
          return STATUS;
        case 2: // _UID_REP_INFOS
          return _UID_REP_INFOS;
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
  private static final _Fields optionals[] = {_Fields._UID_REP_INFOS};
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.STATUS, new org.apache.thrift.meta_data.FieldMetaData("status", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.EnumMetaData(org.apache.thrift.protocol.TType.ENUM, ab_status.class)));
    tmpMap.put(_Fields._UID_REP_INFOS, new org.apache.thrift.meta_data.FieldMetaData("_uid_rep_infos", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, ABDyeingUidRepInfo.class))));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(ABDyeingBatchReply.class, metaDataMap);
  }

  public ABDyeingBatchReply() {
  }

  public ABDyeingBatchReply(
    ab_status status)
  {
    this();
    this.status = status;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public ABDyeingBatchReply(ABDyeingBatchReply other) {
    if (other.isSetStatus()) {
      this.status = other.status;
    }
    if (other.isSet_uid_rep_infos()) {
      List<ABDyeingUidRepInfo> __this___uid_rep_infos = new ArrayList<ABDyeingUidRepInfo>(other._uid_rep_infos.size());
      for (ABDyeingUidRepInfo other_element : other._uid_rep_infos) {
        __this___uid_rep_infos.add(new ABDyeingUidRepInfo(other_element));
      }
      this._uid_rep_infos = __this___uid_rep_infos;
    }
  }

  public ABDyeingBatchReply deepCopy() {
    return new ABDyeingBatchReply(this);
  }

  @Override
  public void clear() {
    this.status = null;
    this._uid_rep_infos = null;
  }

  /**
   * 
   * @see ab_status
   */
  public ab_status getStatus() {
    return this.status;
  }

  /**
   * 
   * @see ab_status
   */
  public ABDyeingBatchReply setStatus(ab_status status) {
    this.status = status;
    return this;
  }

  public void unsetStatus() {
    this.status = null;
  }

  /** Returns true if field status is set (has been assigned a value) and false otherwise */
  public boolean isSetStatus() {
    return this.status != null;
  }

  public void setStatusIsSet(boolean value) {
    if (!value) {
      this.status = null;
    }
  }

  public int get_uid_rep_infosSize() {
    return (this._uid_rep_infos == null) ? 0 : this._uid_rep_infos.size();
  }

  public java.util.Iterator<ABDyeingUidRepInfo> get_uid_rep_infosIterator() {
    return (this._uid_rep_infos == null) ? null : this._uid_rep_infos.iterator();
  }

  public void addTo_uid_rep_infos(ABDyeingUidRepInfo elem) {
    if (this._uid_rep_infos == null) {
      this._uid_rep_infos = new ArrayList<ABDyeingUidRepInfo>();
    }
    this._uid_rep_infos.add(elem);
  }

  public List<ABDyeingUidRepInfo> get_uid_rep_infos() {
    return this._uid_rep_infos;
  }

  public ABDyeingBatchReply set_uid_rep_infos(List<ABDyeingUidRepInfo> _uid_rep_infos) {
    this._uid_rep_infos = _uid_rep_infos;
    return this;
  }

  public void unset_uid_rep_infos() {
    this._uid_rep_infos = null;
  }

  /** Returns true if field _uid_rep_infos is set (has been assigned a value) and false otherwise */
  public boolean isSet_uid_rep_infos() {
    return this._uid_rep_infos != null;
  }

  public void set_uid_rep_infosIsSet(boolean value) {
    if (!value) {
      this._uid_rep_infos = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case STATUS:
      if (value == null) {
        unsetStatus();
      } else {
        setStatus((ab_status)value);
      }
      break;

    case _UID_REP_INFOS:
      if (value == null) {
        unset_uid_rep_infos();
      } else {
        set_uid_rep_infos((List<ABDyeingUidRepInfo>)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case STATUS:
      return getStatus();

    case _UID_REP_INFOS:
      return get_uid_rep_infos();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case STATUS:
      return isSetStatus();
    case _UID_REP_INFOS:
      return isSet_uid_rep_infos();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof ABDyeingBatchReply)
      return this.equals((ABDyeingBatchReply)that);
    return false;
  }

  public boolean equals(ABDyeingBatchReply that) {
    if (that == null)
      return false;

    boolean this_present_status = true && this.isSetStatus();
    boolean that_present_status = true && that.isSetStatus();
    if (this_present_status || that_present_status) {
      if (!(this_present_status && that_present_status))
        return false;
      if (!this.status.equals(that.status))
        return false;
    }

    boolean this_present__uid_rep_infos = true && this.isSet_uid_rep_infos();
    boolean that_present__uid_rep_infos = true && that.isSet_uid_rep_infos();
    if (this_present__uid_rep_infos || that_present__uid_rep_infos) {
      if (!(this_present__uid_rep_infos && that_present__uid_rep_infos))
        return false;
      if (!this._uid_rep_infos.equals(that._uid_rep_infos))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    List<Object> list = new ArrayList<Object>();

    boolean present_status = true && (isSetStatus());
    list.add(present_status);
    if (present_status)
      list.add(status.getValue());

    boolean present__uid_rep_infos = true && (isSet_uid_rep_infos());
    list.add(present__uid_rep_infos);
    if (present__uid_rep_infos)
      list.add(_uid_rep_infos);

    return list.hashCode();
  }

  @Override
  public int compareTo(ABDyeingBatchReply other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = Boolean.valueOf(isSetStatus()).compareTo(other.isSetStatus());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetStatus()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.status, other.status);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSet_uid_rep_infos()).compareTo(other.isSet_uid_rep_infos());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSet_uid_rep_infos()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this._uid_rep_infos, other._uid_rep_infos);
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
    StringBuilder sb = new StringBuilder("ABDyeingBatchReply(");
    boolean first = true;

    sb.append("status:");
    if (this.status == null) {
      sb.append("null");
    } else {
      sb.append(this.status);
    }
    first = false;
    if (isSet_uid_rep_infos()) {
      if (!first) sb.append(", ");
      sb.append("_uid_rep_infos:");
      if (this._uid_rep_infos == null) {
        sb.append("null");
      } else {
        sb.append(this._uid_rep_infos);
      }
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (status == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'status' was not present! Struct: " + toString());
    }
    // check for sub-struct validity
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

  private static class ABDyeingBatchReplyStandardSchemeFactory implements SchemeFactory {
    public ABDyeingBatchReplyStandardScheme getScheme() {
      return new ABDyeingBatchReplyStandardScheme();
    }
  }

  private static class ABDyeingBatchReplyStandardScheme extends StandardScheme<ABDyeingBatchReply> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, ABDyeingBatchReply struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // STATUS
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.status = ab_status.findByValue(iprot.readI32());
              struct.setStatusIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // _UID_REP_INFOS
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list40 = iprot.readListBegin();
                struct._uid_rep_infos = new ArrayList<ABDyeingUidRepInfo>(_list40.size);
                ABDyeingUidRepInfo _elem41;
                for (int _i42 = 0; _i42 < _list40.size; ++_i42)
                {
                  _elem41 = new ABDyeingUidRepInfo();
                  _elem41.read(iprot);
                  struct._uid_rep_infos.add(_elem41);
                }
                iprot.readListEnd();
              }
              struct.set_uid_rep_infosIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, ABDyeingBatchReply struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.status != null) {
        oprot.writeFieldBegin(STATUS_FIELD_DESC);
        oprot.writeI32(struct.status.getValue());
        oprot.writeFieldEnd();
      }
      if (struct._uid_rep_infos != null) {
        if (struct.isSet_uid_rep_infos()) {
          oprot.writeFieldBegin(_UID_REP_INFOS_FIELD_DESC);
          {
            oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct._uid_rep_infos.size()));
            for (ABDyeingUidRepInfo _iter43 : struct._uid_rep_infos)
            {
              _iter43.write(oprot);
            }
            oprot.writeListEnd();
          }
          oprot.writeFieldEnd();
        }
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class ABDyeingBatchReplyTupleSchemeFactory implements SchemeFactory {
    public ABDyeingBatchReplyTupleScheme getScheme() {
      return new ABDyeingBatchReplyTupleScheme();
    }
  }

  private static class ABDyeingBatchReplyTupleScheme extends TupleScheme<ABDyeingBatchReply> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, ABDyeingBatchReply struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      oprot.writeI32(struct.status.getValue());
      BitSet optionals = new BitSet();
      if (struct.isSet_uid_rep_infos()) {
        optionals.set(0);
      }
      oprot.writeBitSet(optionals, 1);
      if (struct.isSet_uid_rep_infos()) {
        {
          oprot.writeI32(struct._uid_rep_infos.size());
          for (ABDyeingUidRepInfo _iter44 : struct._uid_rep_infos)
          {
            _iter44.write(oprot);
          }
        }
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, ABDyeingBatchReply struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      struct.status = ab_status.findByValue(iprot.readI32());
      struct.setStatusIsSet(true);
      BitSet incoming = iprot.readBitSet(1);
      if (incoming.get(0)) {
        {
          org.apache.thrift.protocol.TList _list45 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct._uid_rep_infos = new ArrayList<ABDyeingUidRepInfo>(_list45.size);
          ABDyeingUidRepInfo _elem46;
          for (int _i47 = 0; _i47 < _list45.size; ++_i47)
          {
            _elem46 = new ABDyeingUidRepInfo();
            _elem46.read(iprot);
            struct._uid_rep_infos.add(_elem46);
          }
        }
        struct.set_uid_rep_infosIsSet(true);
      }
    }
  }

}

