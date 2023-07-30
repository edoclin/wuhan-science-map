<template>
    <view>
        <u-skeleton :rows="60" :loading="JSON.stringify(courseDetail) === '{}'" :title="false">
            <u-cell-group v-show="!skeleton">
                <u-cell title="课程名称" :label="courseDetail['courseName']"></u-cell>
                <u-cell title="详细地址" :label="courseDetail['detailAddress']"></u-cell>
                <u-cell title="适宜年龄" :label="courseDetail['targetAge']"></u-cell>
                <u-cell title="开课时间" :label="courseDetail['startingTime']"></u-cell>
            </u-cell-group>

            <u-parse :content="courseDetail['descRichText']" style="margin: 15px 4%;"></u-parse>
        </u-skeleton>

        <view class="col" 
            style="width: 100vw; bottom: 0; text-align: center; background-color: #ffffff; height: 50px;display:flex;">
            <!-- <view class="col" style="width: 50vw; left: 0; text-align: center;align-self:center; bottom: 15px;">
                江城科普一张图
            </view> -->
            <view style="position: absolute; width: 55vw; bottom: 5px;right: 5px;">
                <u-row>
                    <u-col span="6">
                        <view>
                            <u-button :icon="courseDetail.favored ? 'star-fill' : 'star'"
                                iconColor="linear-gradient(90deg, #f9ae3d,#ff4e02)"
                                :text="courseDetail.favored ? '已收藏' : '收藏'" @click="favoriteCourse()" type="warning"
                                shape="circle" color="linear-gradient(90deg, #f9ae3d,#ff4e02)"
                                :loading="loadingFavorite" :disabled="loadingFavorite"
                                :customStyle="{'borderTopRightRadius': '0px', 'borderBottomRightRadius': '0px', 'borderRight': '0.1px solid'}">
                            </u-button>
                        </view>
                    </u-col>
                    <u-col span="6">
                        <view v-if="courseDetail.status == 'BANNED'">
                            <u-button icon="tags" iconColor="linear-gradient(90deg, #ff0000,#ff4e02)"
                                text="暂未开放" @click="bannedCourse()" type="error"
                                shape="circle" color="linear-gradient(-90deg, #ff0000,#ff4e02)"
                                :loading="loadingRegister" :disabled="loadingRegister"
                                :customStyle="{'borderTopLeftRadius': '0px', 'borderBottomLeftRadius': '0px', 'borderLeft': '0.1px solid'}">
                            </u-button>
                        </view>
												<view v-else>
													<u-button icon="tags" iconColor="linear-gradient(90deg, #ff0000,#ff4e02)"
													    :text="courseDetail.registered ? '已报名' : '报名'" @click="registerCourse()" type="success"
													    shape="circle" color="linear-gradient(-90deg, #ff0000,#ff4e02)"
													    :loading="loadingRegister" :disabled="loadingRegister"
													    :customStyle="{'borderTopLeftRadius': '0px', 'borderBottomLeftRadius': '0px', 'borderLeft': '0.1px solid'}">
													</u-button>
												</view>
                    </u-col>
                </u-row>
            </view>
        </view>
        <u-modal :show="showModel" title="提示" showCancelButton @confirm="confirmModel" @cancel="showModel = false">
            <template>
                {{courseDetail.registered ? '确认取消报名?' : '使用用户注册的信息进行报名?'}}
            </template>
        </u-modal>
    </view>
</template>

<script setup>
    import {
        onLoad
    } from "@dcloudio/uni-app"
    import {
        ref
    } from "vue";
    import {
        courseDetailById,
        favorCourseById,
        registerCourseById
    } from "../../api/course";
    import {
        useUserStore
    } from '../../stores/user_store';

    const userStore = useUserStore()
    const courseDetail = ref({})
    const skeleton = ref(true)
    const showModel = ref(false)
    const modelContent = ref('')
    const confirmType = ref(-1)
    const loadingFavorite = ref(false)
    const loadingRegister = ref(false)

    onLoad((option) => {
        courseDetailById(option['id']).then(res => {
					console.log(res);
            setTimeout(() => {
                skeleton.value = false
            }, 500)
            courseDetail.value = res.data
            uni.setNavigationBarTitle({
                title: courseDetail.value.courseName
            })
        })
    })

    const favoriteCourse = () => {
        if (userStore.openid === '') {
            uni.showToast({
                icon: "error",
                title: '用户未登录'
            })
            setTimeout(() => {
                uni.switchTab({
                    url: '/pages/user/user'
                })
            }, 1500)
        } else {
            loadingFavorite.value = true
            favorCourseById(courseDetail.value['id']).then(res => {
                setTimeout(() => {
                    loadingFavorite.value = false
                    courseDetail.value['favored'] = res.data
                }, 500)
            })
        }
    }

    const registerCourse = () => {
        if (userStore.openid === '') {
            uni.showToast({
                icon: "error",
                title: '用户未登录'
            })
            setTimeout(() => {
                uni.switchTab({
                    url: '/pages/user/user'
                })
            }, 1500)
        } else {
					// 填写信息界面
					setTimeout(()=>{
						uni.navigateTo({
							url: '/pages/commit-info/commit-info'
						})
					})
					
          // showModel.value = true
        }
    }
		
		const bannedCourse = () =>{
			uni.showToast({
				icon: "error",
				title: '敬请期待！'
			},2000)
		}

    const confirmModel = () => {
        loadingRegister.value = true
        registerCourseById(courseDetail.value['id']).then(res => {
            showModel.value = false
            if (res.data) {
                uni.showToast({
                    icon: "none",
                    title: '报名成功, 等待系统审核'
                })
            } else {
                uni.showToast({
                    icon: "none",
                    title: '已取消报名!'
                })
            }

            setTimeout(() => {
                loadingRegister.value = false
                courseDetail.value['registered'] = res.data
            }, 500)

        })
    }
</script>

<style scoped>
    .bg {
        background: url(https://dev-edoclin-1304812488.cos.ap-chongqing.myqcloud.com/static%2F%E6%AD%A6%E6%B1%89%E5%B8%82%E7%A7%91%E5%8D%8F.png) no-repeat;
        width: 100%;
        height: 100%;
        background-size: 100% 100%;
    }

    .col {
        overflow: hidden;
        margin-bottom: 0;
        /* background:#f0eff5; */
        width: 100%;
        position: fixed;
        bottom: 0px;
        display: flex;
        justify-content: center;
    }
</style>
