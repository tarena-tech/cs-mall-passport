<template>
  <div>
    <el-container>
      <el-header class="layout-header">
        <img src="@/assets/logo.png" style="width: 60px;height: 60px;position: absolute;top: 0px">
        <h1 style="color: aliceblue;margin-left: 5%">酷鲨商城运营管理平台</h1>
      </el-header>
      <el-container class="layout-body">
        <el-aside class="layout-aside">
          <el-menu
                  background-color="#545c64"
                  router
                  :default-active="this.$router.currentRoute.path"
                  text-color="#fff"
                  active-text-color="#ffd04b"
                  class="el-menu-vertical-demo">
            <el-menu-item index="/">
              <i class="el-icon-s-home"></i>首页
            </el-menu-item>
            <el-submenu index="1">
              <template slot="title">
                <i class="el-icon-s-tools"></i>
                <span>用户管理</span>
              </template>
              <el-menu-item index="/user-list">
                <i class="el-icon-s-custom"></i>用户列表
              </el-menu-item>
              <el-submenu index="1-4">
                <template slot="title">
                  <i class="el-icon-edit-outline"></i>用户日志
                </template>
                <el-menu-item index="/user-login-log">
                  <i class="el-icon-tickets"></i>登录日志</el-menu-item>
                <el-menu-item index="/user-operate-log">
                  <i class="el-icon-document"></i>操作日志</el-menu-item>
              </el-submenu>
              <el-menu-item index="">
                <i class="el-icon-s-grid"></i>角色列表
              </el-menu-item>
              <el-menu-item index="">
                <i class="el-icon-circle-plus"></i>权限列表
              </el-menu-item>
            </el-submenu>

          </el-menu>
        </el-aside>
        <el-main>
          <router-view/>
        </el-main>
      </el-container>
    </el-container>

  </div>
</template>

<script>

  // @ is an alias to /src
  import HelloWorld from '@/components/HelloWorld.vue'

  export default {
    name: 'HomeView',
    components: {
      HelloWorld
    },
  }
</script>
<style>
  .layout-header {
    background-color: #545c64;
    line-height: 60px;

  }

  .layout-body {
    position: absolute;
    top: 60px;
    bottom: 0;
    right: 0;
    left: 0;
  }

  .layout-aside {
    background-color: #545c64;
  }
  .el-menu-item.is-active{
    background:#3d3c3c !important;
  }
</style>