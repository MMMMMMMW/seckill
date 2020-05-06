<template>
  <div id = "app2">
    <el-container >
     
  <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
  <el-form-item label="收货姓名" prop="userName">
    <el-input v-model="ruleForm.userName"></el-input>
  </el-form-item>


    <el-form-item label="手机号码" prop="userId">
    <el-input v-model="ruleForm.userId"></el-input>
  </el-form-item>

    <el-form-item label="收货地址" prop="userAddress">
    <el-input v-model="ruleForm.userAddress"></el-input>
  </el-form-item>

    <el-form-item>
    <el-button type="primary" @click="submitForm()">保存</el-button>
    <el-button @click="resetForm()">重置</el-button>
  </el-form-item>


</el-form>

</el-container>
  </div>
</template>
<style>

#app2 {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
  margin-left: 30%;
}
</style>

<script>
  export default {
    data() {
      return {
        ruleForm: {
          userName: '',
          userId:'',
          userAddress:''
        },
        rules: {
          userName: [
            { required: true, message: '请输入收货姓名', trigger: 'blur' },
          ],
          userId: [
            { required: true, message: '请输入手机号码', trigger: 'blur' },
          ],
          userAddress: [
            { required: true, message: '请输入收货地址', trigger: 'blur' }

          ],
        }
      };
    },

  created(){
      this.ruleForm.userId = this.$cookies.get('userId')
      this.ruleForm.userName = this.$cookies.get('userName')
      this.ruleForm.userAddress = this.$cookies.get('userAddress')

      // axios.get('http://47.112.146.78:8086/seckill/name/test').then(function(resp){
      //       alert(resp.data)
      //   })
  },
    methods: {
      submitForm() {
        // alert(this.ruleForm.userId)

        if (this.ruleForm.userName == ''){
          this.$message.error('请填写收货姓名');
        }
        else if (this.ruleForm.userId == ''){
          this.$message.error('请填写手机号码');
        }
        else if (this.ruleForm.userAddress == ''){
          this.$message.error('请填写收货地址');
        }
        else{
            this.$cookies.set('userId',this.ruleForm.userId)
            this.$cookies.set('userName',this.ruleForm.userName)
            this.$cookies.set('userAddress',this.ruleForm.userAddress)
            this.$message({
                message: '收货信息保存成功',
                type: 'success'
            });
            this.$router.push({path:'/'})
        }

      },

      resetForm() {
        this.ruleForm.userId = '';
        this.ruleForm.userName = '';
        this.ruleForm.userAddress = '';
      }

    }
  }
</script>