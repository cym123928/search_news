<template>
    <div class="id1"></div>
    <div style="margin: 5px 10px 10px;">
        <el-input v-model="search" style="width: 50%;border-style: solid;border-width: 2px;border-radius: 5px" clearable/>
        <el-button size="medium" @click="load()" style="margin-left: 0px;background-color: #6495ED;color: #eeeeee">搜索一下</el-button>
    </div>
</template>

<script>
    export default {
        name: "",
        data(){
            return{
               search:''
            }
        },
        methods:{
            load(){
                sessionStorage.setItem("search",this.search)
                this.$router.push('first')
            }
        }
    }
</script>

<style scoped>

</style>
