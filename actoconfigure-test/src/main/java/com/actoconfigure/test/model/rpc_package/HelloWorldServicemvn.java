// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: grpc_tools.proto

package com.actoconfigure.test.model.rpc_package;

public final class HelloWorldServicemvn {
  private HelloWorldServicemvn() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_actoconfigure_test_model_rpc_package_HelloRequest_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_actoconfigure_test_model_rpc_package_HelloRequest_fieldAccessorTable;
  static final com.google.protobuf.Descriptors.Descriptor
    internal_static_com_actoconfigure_test_model_rpc_package_HelloReply_descriptor;
  static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_com_actoconfigure_test_model_rpc_package_HelloReply_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n\020grpc_tools.proto\022(com.actoconfigure.te" +
      "st.model.rpc_package\"\034\n\014HelloRequest\022\014\n\004" +
      "name\030\001 \001(\t\"\035\n\nHelloReply\022\017\n\007message\030\001 \001(" +
      "\t2\217\001\n\021HelloWorldService\022z\n\010SayHello\0226.co" +
      "m.actoconfigure.test.model.rpc_package.H" +
      "elloRequest\0324.com.actoconfigure.test.mod" +
      "el.rpc_package.HelloReply\"\000BB\n(com.actoc" +
      "onfigure.test.model.rpc_packageB\024HelloWo" +
      "rldServicemvnP\001b\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_com_actoconfigure_test_model_rpc_package_HelloRequest_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_com_actoconfigure_test_model_rpc_package_HelloRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_actoconfigure_test_model_rpc_package_HelloRequest_descriptor,
        new String[] { "Name", });
    internal_static_com_actoconfigure_test_model_rpc_package_HelloReply_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_com_actoconfigure_test_model_rpc_package_HelloReply_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_com_actoconfigure_test_model_rpc_package_HelloReply_descriptor,
        new String[] { "Message", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}