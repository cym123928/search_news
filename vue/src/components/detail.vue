<template>
<div style="width: 1200px;text-align: center;margin-left: 130px;padding: 60px">
   <div style="text-align: center;font-size: 40px;padding-left: 200px">
     {{newsD.title}}
   </div>
   <div style="margin-left: 130px">
     <img :src=newsD.head_img>
   </div>
   <div style="text-align: center;font-size: 25px;width: 100%;margin-left: 130px">
     {{newsD.content}}
   </div>

</div>
</template>

<script>
    export default {
        name: "",
        data(){
           var newsDetail = JSON.parse(sessionStorage.getItem('news'))
            console.log(newsDetail)
            console.log(newsDetail.title)
            return{
                newsD:{
                    title:newsDetail.title,
                    content:newsDetail.content,
                    head_img:newsDetail.head_img
                }
            }
        },
    }
</script>

<style scoped>

</style>
