<template>
    <el-card class="box-card">
        <el-row :gutter="20">
            <el-col :span="12">
                <div style="width: 50%;height: 500px">
                    酷鲨商城后台管理系统
                </div>
            </el-col>
            <el-col :span="12">
                <p style="margin-left: 25%;margin-bottom: 50px">登录</p>
                <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px"
                         class="demo-ruleForm">
                    <el-form-item prop="username">
                        <el-input type="text" v-model="ruleForm.username" autocomplete="off"></el-input>
                    </el-form-item>
                    <el-form-item prop="password">
                        <el-input type="password" v-model="ruleForm.password" autocomplete="off"></el-input>
                    </el-form-item>

                    <el-form-item>
                        <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
                        <el-button @click="resetForm('ruleForm')">重置</el-button>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
    </el-card>
</template>

<script>
    import axios from '@/utils/request.js'

    export default {
        data() {
            var validateName = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入用户名'));
                } else {
                    if (this.ruleForm.username !== '') {
                        callback();
                    }
                }
            };
            var validatePass = (rule, value, callback) => {
                if (value === '') {
                    callback(new Error('请输入密码'));
                } else {
                    callback();
                }
            };
            return {
                ruleForm: {
                    username: '',
                    password: '',
                },
                rules: {
                    username: [
                        {validator: validateName, trigger: 'blur'}
                    ],
                    password: [
                        {validator: validatePass, trigger: 'blur'}
                    ]
                }
            };
        },
        methods: {
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        axios.post('/login', this.ruleForm)
                            .then(response=> {
                                if (response.data.state === 0) {
                                    localStorage.setItem("jwt", response.data.data)
                                    this.$router.push('/')
                                    this.$message.success("登陆成功")
                                } else {
                                    this.$message.error(response.data.message)
                                }
                            })

                    } else {
                        console.log('error submit!!');
                        return false;

                    }
                });
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
            }
        }
    }
</script>

<style>
    .text {
        font-size: 14px;
    }

    .item {
        padding: 18px 0;
    }

    .box-card {
        width: 80%;
        height: 550px;
        margin-left: 10%;
        margin-top: 5%;
    }


</style>