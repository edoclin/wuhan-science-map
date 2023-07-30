<template>
	<view>
		<u-skeleton :loading="JSON.stringify(itemDetail) === '{}'" :animate="true" :rows="40" :title="false">
			<u--image :showLoading="true" :src="itemDetail.accessKeyUrl" width="100vw" height="30vh"></u--image>
			<u-cell>
				<template #title>
					<view style="font-weight: 600;font-size: 20px; text-align: center;">
						{{itemDetail.itemName}}
					</view>
				</template>
				<template #label>
					<view style="margin-top: 8px;font-size: 36rpx; color: #b2b2b2;">
						简介: {{itemDetail.descSimple}}
					</view>
				</template>
			</u-cell>
			<u-parse :content="itemDetail.descRichText" style="margin: 0 4%;"></u-parse>
		</u-skeleton>

		<view class="col"
			style="width: 100vw; bottom: 0; text-align: center; background-color: #ffffff; height: 50px;display:flex;">

			<view style="position: absolute; width: 55vw; bottom: 5px;right: 5px;">
				<u-row>
		 		<u-col span="6">
						<view>
							<u-button :icon="itemDetail.favored ? 'star-fill' : 'star'"
								iconColor="linear-gradient(90deg, #f9ae3d,#ff4e02)"
								:text="itemDetail.favored ? '已收藏' : '收藏'" @click="favoriteCourse()" type="warning"
								shape="circle" color="linear-gradient(90deg, #f9ae3d,#ff4e02)"
								:loading="loadingFavorite" :disabled="loadingFavorite"
								:customStyle="{'borderTopRightRadius': '0px', 'borderBottomRightRadius': '0px', 'borderRight': '0.1px solid'}">
							</u-button>
						</view>
					</u-col>
					<u-col span="6">
						<view>
							<u-button icon="tags" iconColor="linear-gradient(90deg, #ff0000,#ff4e02)"
								:text="itemDetail.registered ? '已报名' : '报名'" @click="registerCourse()" type="success"
								shape="circle" color="linear-gradient(-90deg, #ff0000,#ff4e02)"
								:loading="loadingRegister" :disabled="loadingRegister"
								:customStyle="{'borderTopLeftRadius': '0px', 'borderBottomLeftRadius': '0px', 'borderLeft': '0.1px solid'}">
							</u-button>
						</view>
					</u-col>
				</u-row>
			</view>
		</view>
		<!-- <u-modal :show="showModel" title="提示" showCancelButton @confirm="confirmModel" @cancel="showModel = false">
			<template>
				{{itemDetail.registered ? '确认取消报名?' : '使用用户注册的信息进行报名?'}}
			</template>
		</u-modal> -->
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
		tripItemDetailById,
        checkItemStockById
	} from "../../api/trip"
	import {
		courseDetailById,
		favorCourseById,
		registerCourseById
	} from "../../api/course";

	import {
		useUserStore
	} from '../../stores/user_store';
	const itemDetail = ref({})
	onLoad((option) => {
		tripItemDetailById(option.id).then(res => {
			itemDetail.value = res.data
		})
	})


	const userStore = useUserStore()
	const skeleton = ref(true)
	const showModel = ref(false)
	const confirmType = ref(-1)
	const loadingFavorite = ref(false)
	const loadingRegister = ref(false)
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
			favorCourseById(itemDetail.courseId).then(res => {
				setTimeout(() => {
					loadingFavorite.value = false
					itemDetail.favored = res.data
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
            // 判断当前活动是否还能报名
            checkItemStockById(itemDetail.value.id).then(res =>{
                console.log(res);
                
                if(res.code === 5000){
                    uni.showToast({
                        icon:"error",
                        title: "活动人数已满"
                    })
                }else{
                    uni.navigateTo({
                        url: `/pages/commit-info/commit-info?id=${itemDetail.value.id}`
                    })
                }
            })
            
            
            
		}
	}
	const confirmModel = () => {
		loadingRegister.value = true
		registerCourseById(itemDetail.courseId).then(res => {
			console.log(res)
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
				itemDetail.registered = res.data
			}, 500)

		})
	}
</script>

<style scoped>
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
