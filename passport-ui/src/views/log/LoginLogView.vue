<template>
    <div>
        <el-container>
            <el-header class="el-header1">
                <el-breadcrumb separator-class="el-icon-arrow-right" style="font-size: 16px">
                    <el-breadcrumb-item :to="{ path: '/' }">
                        <i class="el-icon-s-promotion"></i> 首页
                    </el-breadcrumb-item>
                    <el-breadcrumb-item>登录日志</el-breadcrumb-item>
                </el-breadcrumb>
            </el-header>
            <el-main>
                <el-form :inline="true" :model="LoginQuery" ref="userQuery" class="demo-form-inline"
                         v-show="showEachState">
                    <el-form-item label="用户名称">
                        <el-input v-model="LoginQuery.username" placeholder="用户名称"></el-input>
                    </el-form-item>
                    <el-form-item label="用户状态">
                        <el-select v-model="LoginQuery.state" placeholder="请选择">
                            <el-option
                                    v-for="item in options"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item style="margin-left: 1%">
                        <el-button type="primary" @click="loadLoginLogList">查询</el-button>
                        <el-button @click="resetForm">重置</el-button>
                    </el-form-item>
                </el-form>
                <el-row style="margin-bottom: 10px">
                    <el-button type="danger" plain size="mini">导出数据</el-button>
                    <el-button icon="el-icon-refresh" circle size="mini" style="float: right"
                               @click="loadLoginLogList"></el-button>
                    <el-button icon="el-icon-search" circle size="mini" style="float: right"
                               @click="showEach"></el-button>
                </el-row>
                <el-table
                        :data="tableData"
                        border
                        style="width: 100%">
                    <el-table-column
                            prop="id"
                            label="操作编号"
                            width="100"
                            align="center">
                    </el-table-column>
                    <el-table-column
                            prop="username"
                            label="用户名"
                            width="120"
                            align="center">
                    </el-table-column>
                    <el-table-column
                            prop="ip"
                            label="登录IP"
                            width="120"
                            align="center">
                    </el-table-column>
                    <el-table-column
                            prop="userAgent"
                            label="浏览器"
                            width="180"
                            align="center">
                    </el-table-column>
                    <el-table-column
                            prop="state"
                            label="状态"
                            width="100"
                            align="center">
                        <template slot-scope="scope">
                            <el-button type="success" v-if="scope.row.state==1">成功</el-button>
                            <el-button type="danger" v-else>失败</el-button>
                        </template>
                    </el-table-column>
                    <el-table-column
                            prop="detail"
                            label="操作详情"
                            width="180"
                            align="center">
                    </el-table-column>
                    <el-table-column
                            prop="gmtLogin"
                            label="登录时间"
                            align="center">
                    </el-table-column>
                    <el-table-column
                            label="删除"
                            width="100"
                            align="center">
                        <template slot-scope="scope">
                            <el-popconfirm
                                    title="确定删除用户吗？" @confirm="handleDelete(scope.row.id)">
                                <el-button slot="reference" type="danger" icon="el-icon-delete" size="mini"
                                           circle></el-button>
                            </el-popconfirm>
                        </template>
                    </el-table-column>
                </el-table>
            </el-main>
        </el-container>


    </div>
</template>
<script>
    import axios from '@/utils/request.js'

    export default {
        data() {
            return {
                showEachState: true,
                tableData: [],
                LoginQuery: {
                    username: null,
                    state: ''
                },

                options: [{
                    value: '0',
                    label: '失败'
                }, {
                    value: '1',
                    label: '成功'
                }],
                dialogFormVisible: false,
                formLabelWidth: '120px',
                updateUserFromVisible: false,
                redio: '0',
            }
        },
        methods: {
            showEach() {
                if (this.showEachState == true) {
                    this.showEachState = false;
                } else {
                    this.showEachState = true;
                }
            },
            loadLoginLogList() {
                let jwt = localStorage.getItem("jwt")
                axios.create({'headers': {'Authorization': jwt}})
                    .post("http://localhost:8080/log/login/list",this.LoginQuery)
                    .then(response => {
                        let r = response.data
                        console.log(this.LoginQuery)
                        if (r.state == 0) {
                            this.tableData = r.data
                        } else if (r.state == -1) {
                            localStorage.clear();
                        } else {
                            this.$message.error("系统繁忙，请稍后再试")
                        }
                    })

            },
            handleDelete(id) {
                console.log(id)
                let jwt = localStorage.getItem("jwt")
                axios.create({'headers': {'Authorization': jwt}})
                    .post("http://localhost:8080/log/login/delete/"+id,this.LoginQuery)
                    .then(response => {
                        let r = response.data
                        if (r.state == 0) {
                            this.$message.error("删除成功")
                            this.loadLoginLogList()
                        } else if (r.state == -1) {
                            localStorage.clear();
                            this.$message.error("登录失效，请重新登录")
                        } else {
                            this.$message.error("系统繁忙，请稍后再试")
                        }
                    })
            },
            resetForm(){
                this.LoginQuery.username=null
                this.LoginQuery.state=null
            }
        },
        created() {
            this.loadLoginLogList();

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