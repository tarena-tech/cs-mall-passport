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
                        <el-button type="primary" @click="loadAdminList(null,null)">查询</el-button>
                        <el-button @click="resetForm('userQuery')">重置</el-button>
                    </el-form-item>
                </el-form>
                <el-row style="margin-bottom: 10px">
                    <el-button type="primary" plain size="mini" @click="dialogFormVisible = true">添加用户</el-button>
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
                        layout="prev, pager, next"
                        :total="pageInfo.total"
                        :page-size="pageInfo.pageSize"
                        :current-page="pageInfo.page"
                        @current-change="changePage"
                        :page-count="pageInfo.totalPage">
                </el-pagination>
<!--                style="position: absolute;bottom: 5%;right: 30%"-->
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

        <el-dialog title="修改用户" :visible.sync="updateUserFromVisible">
            <el-form :model="user">
                <el-row>
                    <el-col :span="12">
                        <el-form-item label="用户昵称" :label-width="formLabelWidth">
                            <el-input v-model="user.nickname" autocomplete="off">
                            </el-input>
                        </el-form-item>
                        <el-form-item label="用户邮箱" :label-width="formLabelWidth">
                            <el-input v-model="user.email" autocomplete="off"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="手机号码" :label-width="formLabelWidth">
                            <el-input type="phone" v-model="user.phone" autocomplete="off"></el-input>
                        </el-form-item>
                        <el-form-item label="用户状态" v-model="user.enable" :label-width="formLabelWidth">
                            <template>
                                <el-radio v-model="redio" label="0">禁用</el-radio>
                                <el-radio v-model="redio" label="1">启用</el-radio>
                            </template>
                        </el-form-item>
                    </el-col>
                </el-row>

            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="updateUserFromVisible = false">取 消</el-button>
                <el-button type="primary" @click="updateUser">确 定</el-button>
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
                user: {
                    nickname: null,
                    phone: null,
                    email: null,
                    enable: null,
                },
                dialogFormVisible: false,
                formLabelWidth: '120px',
                updateUserFromVisible: false,
                redio: '0',
                pageInfo:{}
            }
        },
        methods: {
            handleEdit(id) {
                this.updateUserFromVisible = true
                let jwt = localStorage.getItem("jwt")
                axios.create({'headers': {'Authorization': jwt}})
                    .post("/" + id)
                    .then(response => {
                        let r = response.data
                        this.redio = "" + r.data.enable + ''
                        if (r.state == 0) {
                            this.user = r.data;
                        } else if (r.state == -1) {
                            localStorage.clear();
                        } else {
                            this.$message.error("系统繁忙，请稍后再试")
                        }
                    })
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
            loadAdminList(page,pageSize) {
                let jwt = localStorage.getItem("jwt")
                if (page==null){
                    page=1

                }
                if (pageSize==null){
                    pageSize=8
                }

                if (this.showEachState == true){
                    pageSize=6
                }
                axios.create({'headers': {'Authorization': jwt}})
                    .post("/user-list/"+page+"/"+pageSize, this.userQuery)
                    .then(response => {
                        let r = response.data
                        console.log(r)
                        if (r.state == 0) {
                            this.pageInfo=r.data;
                            console.log("this.pageInfo")
                            console.log( this.pageInfo)
                            this.tableData = this.pageInfo.list
                        } else if (r.state == -1) {
                            localStorage.clear();
                        } else {
                            this.$message.error("系统繁忙，请稍后再试")
                        }
                    })


            },
            handleChangeEnable(user) {
                let enableText = ['禁用', '启用'];
                let url = 'http://localhost:8080/user/' + user.id;
                if (user.enable == 1) {
                    url += '/enable';
                } else {
                    url += '/disable';
                }

                axios.create({'headers': {'Authorization': localStorage.getItem('jwt')}})
                    .post(url).then((response) => {
                    let responseBody = response.data;
                    console.log(responseBody)
                    if (responseBody.state == 0) {
                        this.$message({
                            message: responseBody.data,
                            type: 'success'
                        });
                    } else {
                        this.$message.error(responseBody.message);
                    }
                });

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
                this.loadAdminList(null,null)
            },
            addUser() {
                this.dialogFormVisible = false
                let jwt = localStorage.getItem("jwt")
                axios.create({'headers': {'Authorization': jwt}})
                    .post("/add-user", this.userAdd)
                    .then(response => {
                        let r = response.data
                        console.log(r.message)
                        if (r.state == 0) {
                            this.$message.success(r.data)
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
                            this.$message.error(r.message)
                        }
                    })
            },
            updateUser() {
                this.updateUserFromVisible = false
                this.user.enable = parseInt(this.redio)
                console.log(this.user)
                let jwt = localStorage.getItem("jwt")
                axios.create({'headers': {'Authorization': jwt}})
                    .post("/update", this.user)
                    .then(response => {
                        let r = response.data
                        console.log(r)
                        if (r.state == 0) {
                            this.$message.success("修改成功")
                            this.loadAdminList()
                        } else if (r.state == -1) {
                            localStorage.clear();
                        } else {
                            this.$message.error("系统繁忙，请稍后再试")
                        }
                    })
            },
            changePage(a){
                this.pageInfo.page=a;
                this.loadAdminList(a,null)
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