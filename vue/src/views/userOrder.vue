<template>
  <div>


  <el-container>
    <el-main>
      <el-table :data="goods" stripe = true   :default-sort = "{prop: 'date', order: 'descending'}">
        <el-table-column prop="name" label="名字" width="140" >
        </el-table-column>
        <el-table-column prop="price" label="价格" width="120">
        </el-table-column>
        <el-table-column prop="createTime" label="秒杀时间" >
        </el-table-column>
        <el-table-column prop="userName" label="收货人">
        </el-table-column>
        <el-table-column prop="userAddress" label="收货地址">
        </el-table-column>

      </el-table>
    </el-main>
    </el-container>

    <el-pagination 
      background
      layout="prev, pager, next"
      :total="goodsNumber"
      :current-page="currentPage"
      
      @current-change="page">
    </el-pagination>

  </div>
</template>

<script>


</script>

<style>

#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>

<script>
  export default{
    data(){
        return{
            orders:[],
            currentPage:[]
        }
    },
    created(){
        const _this = this
        this.currentPage = this.$cookies.isKey('currentPage')?this.$cookies.get('currentPage'):1

        axios.get('http://47.112.146.78:8086/seckill/'+(this.currentPage-1)*10+'/list').then(function(resp){
            // console.log(resp)
            _this.goods = resp.data
        })

        axios.get('http://47.112.146.78:8086/seckill/goods/number').then(function(resp){

            _this.goodsNumber = resp.data
        })

        axios.get('http://47.112.146.78:8086/seckill/time/now').then(function(resp){

            _this.nowTime = resp.data.data
        })
    },

    methods:{
        //点击页数
        page(currentPage){
            const _this = this
            axios.get('http://47.112.146.78:8086/seckill/time/now').then(function(resp){
                _this.nowTime = resp.data.data
            })

            axios.get('http://47.112.146.78:8086/seckill/'+(currentPage-1)*10+'/list').then(function(resp){
                _this.goods = resp.data
            })
            
            this.$cookies.set('currentPage',currentPage)
        },

        killConfirm(id) {
            var userMessage = '收货人姓名：'+this.$cookies.get('userName')
                            +'<br>收货人手机号码：'+this.$cookies.get('userId')
                            +'<br>收货人地址：'+this.$cookies.get('userAddress')
            const _this = this
            // alert(userMessage)
            this.$confirm(userMessage, '请确认信息', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning',
                dangerouslyUseHTMLString:true
            }).then(() => {
                const _this = this
                axios.get('http://47.112.146.78:8086/seckill/'+id+'/exposer').then(function(resp){
                    
                    if (resp.data.success == false){
                        _this.$message({
                        type: 'error',
                        message: resp.data.error
                    });}
                    else{
                        axios.post('http://47.112.146.78:8086/seckill/'+id+'/'+resp.data.data.md5+'/execution').then(function(_resp){
                            if (_resp.data.success == false){
                                _this.$message({
                                type: 'error',
                                message: _resp.data.error
                            });}
                            else{
                                _this.$message({
                                type: 'success',
                                message: '秒杀成功'
                            });}
                            setTimeout(function(){
                                location.reload();
                            },800)
                        })
                    }


                })

            }).catch(() => {
                this.$message({
                    type: 'warning',
                    message: '秒杀取消'
                });
                setTimeout(function(){
                    location.reload();
                },800) 
            });

        }
      



    }
}
</script>
