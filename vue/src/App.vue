<template>
  <div id="app">
    <el-container>
    <el-header style="text-align: right; font-size: 20px">
      <el-button type="primary" size="medium" @click="seckillList()" plain>主页</el-button>
        <el-button type="danger" size="medium" @click="userInfo()" plain>填写收货信息</el-button>
        <el-button type="warning" size="medium" @click="addGoods()" plain>随机添加秒杀商品</el-button>
    </el-header>
  </el-container>

      <router-view></router-view>
  </div>


</template>

<script>

</script>

<style>
  .el-header {
    background-color: #B3C0D1;
    color: #333;
    line-height: 60px;
  }
  
  .el-aside {
    color: #333;
  }
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 10px;
}
</style>

<script>
  export default{
    data(){
        return{

        }
    },
    created(){

    },

    methods:{
      seckillList(){
        this.$router.push({path:'/'})
      },

      userInfo(){
        this.$router.push({path:'/seckill/userInfo'})
      },

      addGoods(){
        axios.get('http://47.112.146.78:8086/goods/add').then(function(resp){
            
        })
        this.$message({
            type: 'success',
            message: '添加成功'
        });
        setTimeout(function(){
            location.reload();
        },800)
      }
    }
}
</script>
