<template>
    <div>
        <el-container>
            <el-header class="el-header1">
                <el-breadcrumb separator-class="el-icon-arrow-right" style="font-size: 16px">
                    <el-breadcrumb-item :to="{ path: '/' }">
                        <i class="el-icon-s-promotion"></i> 首页
                    </el-breadcrumb-item>
                    <el-breadcrumb-item>管理员列表</el-breadcrumb-item>
                </el-breadcrumb>
            </el-header>
            <el-main>
                <el-form :inline="true" :model="userQuery" ref="userQuery" class="demo-form-inline"
                         v-show="showEachState">
                    <el-form-item label="用户名称">
                        <el-input v-model="userQuery.username" placeholder="用户名称"></el-input>
                    </el-form-item>
                    <el-form-item label="用户昵称">
                        <el-input v-model="userQuery.nickname" placeholder="用户昵称"></el-input>
                    </el-form-item>

                    <el-form-item label="邮箱">
                        <el-input v-model="userQuery.email" placeholder="邮箱"></el-input>
                    </el-form-item>
                    <el-form-item label="手机号码">
                        <el-input v-model="userQuery.phone" placeholder="手机号码"></el-input>
                    </el-form-item>
                    <el-form-item label="用户状态">
                        <el-select v-model="userQuery.enable" placeholder="请选择">
                            <el-option
                                    v-for="item in options"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <!--            <el-form-item label="创建时间">-->
                    <!--                <el-date-picker-->
                    <!--                        v-model="userQuery.gmtCreate"-->
                    <!--                        type="daterange"-->
                    <!--                        range-separator="至"-->
                    <!--                        start-placeholder="开始日期"-->
                    <!--                        end-placeholder="结束日期">-->
                    <!--                </el-date-picker>-->
                    <!--            </el-form-item>-->
                    <el-form-item style="margin-left: 1%">
                        <el-button type="primary" @click="loadAdminList">查询</el-button>
                        <el-button @click="resetForm('userQuery')">重置</el-button>
                    </el-form-item>
                </el-form>
                <el-row style="margin-bottom: 10px">
                    <el-button type="primary" plain size="mini" @click="dialogFormVisible = true">添加用户</el-button>
                    <el-button type="success" plain size="mini">导入数据</el-button>
                    <el-button type="danger" plain size="mini">导出数据</el-button>
                    <el-button icon="el-icon-refresh" circle size="mini" style="float: right"
                               @click="loadAdminList"></el-button>
                    <el-button icon="el-icon-search" circle size="mini" style="float: right"
                               @click="showEach"></el-button>
                </el-row>
                <el-table
                        :data="tableData"
                        border
                        style="width: 100%">
                    <el-table-column
                            prop="id"
                            label="ID"
                            width="60"
                            align="center">
                    </el-table-column>
                    <el-table-column
                            prop="username"
                            label="用户名"
                            width="120"
                            align="center">
                    </el-table-column>
                    <el-table-column
                            prop="nickname"
                            label="昵称"
                            width="120"
                            align="center">
                    </el-table-column>
                    <el-table-column
                            prop="phone"
                            label="手机号码"
                            width="150"
                            align="center">
                    </el-table-column>
                    <el-table-column
                            prop="email"
                            label="电子邮件"
                            width="180"
                            align="center">
                    </el-table-column>
                    <el-table-column
                            prop="gmtCreate"
                            label="创建时间"
                            align="center">
                    </el-table-column>
                    <el-table-column
                            label="是否启用"
                            width="80"
                            align="center">
                        <template slot-scope="scope">
                            <el-switch
                                    @change="handleChangeEnable(scope.row)"
                                    v-model="scope.row.enable"
                                    :active-value="1"
                                    :inactive-value="0"
                                    active-color="#13ce66"
                                    inactive-color="#999"
                            ></el-switch>
                        </template>
                    </el-table-column>
                    <el-table-column
                            label="操作"
                            width="100"
                            align="center">
                        <template slot-scope="scope">
                            <el-button type="primary" icon="el-icon-edit" size="mini" circle
                                       @click="handleEdit(scope.row.id)" style="margin-right: 10px "></el-button>
                            <el-popconfirm
                                    title="确定删除用户吗？" @confirm="handleDelete(scope.row.id)">
                                <el-button slot="reference" type="danger" icon="el-icon-delete" size="mini"
                                           circle></el-button>
                            </el-popconfirm>
                        </template>
                    </el-table-column>
                </el-table>

            </el-main>
            <el-footer class="footer">
                <el-pagination
                        style="position: absolute;bottom: 5%;right: 30%"
                        layout="prev, pager, next"
                        :total="50">
                </el-pagination>
            </el-footer>
        </el-container>
        <el-dialog title="新增用户" :visible.sync="dialogFormVisible">
            <el-form :model="userAdd">
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="用户名称" :label-width="formLabelWidth">
                            <el-input v-model="userAdd.username" autocomplete="off">
                            </el-input>
                        </el-form-item>
                        <el-form-item label="用户密码" :label-width="formLabelWidth">
                            <el-input type="password" v-model="userAdd.password" autocomplete="off"></el-input>
                        </el-form-item>
                        <el-form-item label="用户邮箱" :label-width="formLabelWidth">
                            <el-input v-model="userAdd.email" autocomplete="off"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="用户昵称" :label-width="formLabelWidth">
                            <el-input v-model="userAdd.nickname" autocomplete="off">
                            </el-input>
                        </el-form-item>
                        <el-form-item label="手机号码" :label-width="formLabelWidth">
                            <el-input type="phone" v-model="userAdd.phone" autocomplete="off"></el-input>
                        </el-form-item>
                        <el-form-item label="用户状态" :label-width="formLabelWidth">
                            <el-select v-model="userAdd.enable" placeholder="请选择">
                                <el-option
                                        v-for="item in options"
                                        :key="item.value"
                                        :label="item.label"
                                        :value="item.value">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="addUser">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>
<script>
    import axios from '@/utils/request.js'

    export default {
        data() {
            return {
                tableData: [],
                userQuery: {
                    username: null,
                    password: null,
                    nickname: null,
                    phone: null,
                    email: null,
                    enable: null,
                    gmtCreate: null,
                    gmtModified: null
                },
                userAdd: {
                    username: null,
                    password: 123456,
                    nickname: null,
                    phone: null,
                    email: null,
                    enable: null,
                },
                showEachState: true,
                options: [{
                    value: '0',
                    label: '禁用'
                }, {
                    value: '1',
                    label: '启用'
                }],
                dialogFormVisible: false,
                formLabelWidth: '120px'
            }
        },
        methods: {
            handleEdit(id) {

            },
            handleDelete(id) {
                let jwt = localStorage.getItem("jwt")
                axios.create({'headers': {'Authorization': jwt}})
                    .post("/deleteById/" + id)
                    .then(response => {
                        let r = response.data

                        console.log(r)
                        if (r.state == 0) {
                            this.$message.success("删除成功")
                            this.loadAdminList()
                        } else if (r.state == -1) {
                            localStorage.clear();
                        } else {
                            this.$message.error("系统繁忙，请稍后再试")
                        }
                    })
            },
            loadAdminList() {
                console.log(this.userQuery)
                let jwt = localStorage.getItem("jwt")
                axios.create({'headers': {'Authorization': jwt}})
                    .post("/user-list", this.userQuery)
                    .then(response => {
                        let r = response.data

                        console.log(r)
                        if (r.state == 0) {
                            this.tableData = r.data
                        } else if (r.state == -1) {
                            localStorage.clear();
                        } else {
                            this.$message.error("系统繁忙，请稍后再试")
                        }
                    })


            },
            handleChangeEnable(admin) {
                let enableText = ['禁用', '启用'];
                let url = 'http://localhost:8080/user/' + admin.id;
                if (admin.enable == 1) {
                    url += '/enable';
                } else {
                    url += '/disable';
                }
                console.log('url = ' + url);
                // this.axios
                //     .create({'headers': {'Authorization': localStorage.getItem('jwt')}})
                //     .post(url).then((response) => {
                //     let responseBody = response.data;
                //     if (responseBody.state == 20000) {
                //         let message = '将【' + admin.username + '】的账号状态改为【' + enableText[admin.enable] + '】成功';
                //         console.log(message);
                //         this.$message({
                //             message: message,
                //             type: 'success'
                //         });
                //     } else {
                //         this.$message.error(responseBody.message);
                //     }
                // });

            },
            resetForm(formName) {
                this.userQuery.username = null;
                this.userQuery.nickname = null;
                this.userQuery.phone = null;
                this.userQuery.email = null;
                this.userQuery.enable = null;
                this.userQuery.gmtCreate = null;
                this.userQuery.gmtModified = null;

                this.loadAdminList()
            },
            showEach() {
                if (this.showEachState == true) {
                    this.showEachState = false;
                } else {
                    this.showEachState = true;
                }
            },
            addUser(){
                this.dialogFormVisible = false
                let jwt = localStorage.getItem("jwt")
                axios.create({'headers': {'Authorization': jwt}})
                    .post("/add-user", this.userAdd)
                    .then(response => {
                        let r = response.data

                        if (r.state == 0) {
                            this.$message.success(r.message)
                            this.loadAdminList()
                            this.userAdd.username = null;
                            this.userAdd.nickname = null;
                            this.userAdd.password = 123456;
                            this.userAdd.phone = null;
                            this.userAdd.email = null;
                            this.userAdd.enable = null;
                        } else if (r.state == -1) {
                            localStorage.clear();
                        } else {
                            this.$message.error("系统繁忙，请稍后再试")
                        }
                    })
            }
        },
        created() {
            this.loadAdminList()
        }
    }
</script>
<style>

    .el-header1 {
        color: #333;
        text-align: center;
        height: 30px !important;
    }

    .footer {
        text-align: center;
        height: 30px !important;
    }
</style>