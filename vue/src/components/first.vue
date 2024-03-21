<template>
    <div style="position:relative;left:5px;top:30px;width:800px;height:50px">
        <el-input  v-model="search" style="width: 75%;border-radius: 5px;border-style: solid;border-width: 1px" clearable placeholder="请输入关键字" />
        <el-button @click="load()" style="margin-left: 0px;background-color: #6495ED;color: #eeeeee">搜索一下</el-button>
    </div>
    <div v-if="total!==-1" style="font-size: 15px;text-align:left ;position: absolute;left: 60px;top:75px">约{{total}}个结果</div>
    <div style="padding:60px;margin-left: 2px">
       <ul>
           <li v-for="item in tableData" style="text-align: left">
               <a @click="getDetail(item)" v-html="brightenKeyword(item.title,search)" style="font-size: 23px;color: #0000FF"></a>
               <div>
                   <a @click="getDetail(item)" style="font-size: 13px;color: #008000">{{item.url}}</a>
               </div>
               <div style="font-size: 18px;width: 850px">{{item.description}}</div>
               <span style="font-size: 15px;text-align: right">分类:</span>
               <span style="font-size: 15px" v-html="brightenKeyword(item.category,search)"></span>
               <span style="font-size: 15px;margin-left: 20px;text-align: right">来源:{{item.source}}</span>
           </li>
       </ul>
        <div v-if="total!==-1" style="margin: 10px 10px;text-align: left">
            <el-pagination
                    v-model:currentPage="currentPage"
                    :page-size="pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="total"
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
            >
            </el-pagination>
        </div>
    </div>
    <div style="margin: 0px 50px">
        <el-dialog title="添加" v-model="dialogVisible" width="30%">
            <el-form :model="user">

            </el-form>
            <span style="margin: 0 80px">
                    <el-button type="primary" @click="dialogVisible=false">确 定</el-button>
                    </span>
        </el-dialog>
    </div>

</template>

<script>
    import request from "@/utils/request";
    export default {
        name: "",
        data(){
            return{
                search:sessionStorage.getItem('search'),
                dialogVisible:false,
                currentPage:1,
                pageSize:20,
                total:-1,
                tableData:[]
            }
        },
        created() {
            this.load()
        },
        methods:{
            load(){
                console.log("loadjin")
                request.get('/api/sc/list',{
                    params:{
                        queryString: this.search,
                        currentPage:this.currentPage
                    }
                }).then(res=>{
                    console.log("aaa")
                    this.tableData = res.data.item
                    this.total = res.data.recordCount
                    console.log(this.total,"total")
                })
            },

            //搜索高亮
            brightenKeyword(val, keyword) {
                if (keyword.length > 0) {
                    let keywordArr = keyword.split("");
                    val = val + "";
                    keywordArr.forEach(item => {
                        if (val.indexOf(item) !== -1 && item !== "") {
                            val = val.replace(
                                new RegExp(item,'g'),
                                '<font color="#f75353">' + item + "</font>"
                            );
                        }
                    });
                    return val;
                } else {
                    return val;
                }
            },
           getDetail(e){
             sessionStorage.setItem('news',JSON.stringify(e))
             this.$router.push('detail')
           },

            handleCurrentChange(currentPage){
                this.currentPage=currentPage;
                this.load();
            }
        }
    }
</script>

<style scoped>

</style>
