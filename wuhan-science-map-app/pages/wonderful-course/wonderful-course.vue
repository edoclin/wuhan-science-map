<template>

	<view>
		<!-- 智慧行 -->
		<view class="travel-main">
			<view class="header-wrap">
				<image class="header-image" src="../../static/mapIcon.png"></image>
				<view class="header-item">智慧行
					<view style="font-size: 28rpx; color:mediumspringgreen;">
						（公益）
					</view>
				</view>
				<view style="text-align: center;font-size: 32rpx;" @click="toTripList">
					更多
					<image style="width: 36rpx;height: 36rpx;margin-top: 8rpx;" src="../../static/HM-PersonalCenter/to.png"></image>
				</view>
			</view>
			
			<view>
				<u-skeleton :loading="skeleton" :animate="true" :rows="20" :title="false">
					<u-empty v-if="zhihuiTripTop.length === 0" mode="list"
						icon="http://cdn.uviewui.com/uview/empty/list.png" text="来早了...">
					</u-empty>
					<view v-else>
						<view class="goods-item-wrap" @click="toTripDetail(item.id,item.tripName)"
							v-for="(item, index) in zhihuiTripTop" :key="index">
							<image :src="item.accessKeyUrl" class="goods-image" mode="aspectFit" />								
							<view class="goods-content-wrap">
								<view class="goods-name">{{ item.tripName }}</view>					
								<view class="goods-stock-wrap">
									<view class="goods-stock">简介：{{ item.descSimple.length > 30? item.descSimple.substring(0,30)+'...' : item.descSimple }}</view>
									<view class="goods-sell-count">时间：{{item.startingTime}} </view>
								</view>
							</view>
						</view>
					</view>
				</u-skeleton>
			</view>
		</view>
		
		<!-- 课程展示 -->
		<view class="goods-main">
			<view class="header-wrap">
				<image class="header-image" src="../../static/courseIcon.png"></image>
				<view class="header-item">课程列表</view>
			</view>
			<view>
				<u-skeleton :loading="skeleton" :animate="true" :rows="40" :title="false">
					<u-empty v-if="wonderfulCourseList.length === 0" mode="list"
						icon="http://cdn.uviewui.com/uview/empty/list.png" text="来早了...">
					</u-empty>
					<view v-else>
			
			
						<view class="goods-item-wrap" @click="toCourseDetail(item.id)"
							v-for="(item, index) in wonderfulCourseList" :key="index">
							<image :src="item.wonderfulImageAccessKeyUrl" class="goods-image" mode="aspectFit" />
									
							<view class="goods-content-wrap">
								<view class="goods-name">{{ item.courseName }}</view>
									
								<view class="goods-stock-wrap">
									<view class="goods-stock">课程地点：{{ item.detailAddress }}</view>
									<view class="goods-sell-count">开始时间：{{item.startingTime}} </view>
								</view>
								<view class="goods-price">适应人群：{{item.targetAge}}</view>
							</view>
						</view>
					</view>
				</u-skeleton>
			</view>
		</view>
		
	</view>

</template>

<script setup>
	import {
		ref
	} from "vue";
	import {
		wonderfulCourse
	} from "../../api/course";
	import {
		zhihuiTrip
	} from "../../api/trip"

	const wonderfulCourseList = ref([])
	const zhihuiTripTop = ref([])
	const skeleton = ref(false)
	const subDescSimple = ref('')

	wonderfulCourse().then(res => {
		setTimeout(() => {
			skeleton.value = false
		}, 300)
		wonderfulCourseList.value = res.data
	})

	zhihuiTrip(1).then(res => {
		console.log(res)
		setTimeout(() => {
			skeleton.value = false
		}, 300)
		zhihuiTripTop.value = res.data
	})

	const toTripList = () =>{
		uni.navigateTo({
			url: '/pages/zhihuiTrip/zhihuiTrip'
		})
	}

	const toTripDetail = (id,name) => {
		uni.navigateTo({
			url: `/pages/zhihuiTrip-item/zhihuiTrip-item?id=${id}&name=${name}`
		})
	}

	const toCourseDetail = (id) => {
		uni.navigateTo({
			url: `/pages/course-detail/course-detail?id=${id}`
		})
	}
</script>

<style lang="scss" scoped>
	.travel-main {
		min-height: auto;
		background: #f7f8fa;
		padding-top: 0rpx;

		.header-wrap {
			background: #FFFFFF;
			display: flex;
			padding: 24rpx;
			margin-top: 12rpx;
			border-radius: 8rpx;

			.header-image {
				flex-shrink: 2rpx;
				width: 64rpx;
				height: 64rpx
			}

			.header-item {
				margin: 6rpx auto 0 12rpx;
				font-size: 40rpx;
				font-color: #333333;
				font-weight: bold;
				position: relative;
				display: flex;
			}
		}
		.goods-item-wrap {
			background: #FFFFFF;
			display: flex;
			padding: 12rpx;
			margin: 10rpx;
			border-radius: 8rpx;
		
			.goods-image {
				width: 200rpx;
				height: 200rpx;
				border-radius: 12rpx;
				flex: none;
			}
		
			.goods-content-wrap {
				margin-top: 12rpx;
				padding-left: 24rpx;
		
				.goods-name {
					font-size: 35rpx;
					font-weight: bold;
					text-align: center;
				}
		
				.goods-stock-wrap {
					color: #969696;
					font-size: 26rpx;
					margin-top: 18rpx;
		
					.goods-stock {
						margin-right: 24rpx;
					}
					.goods-sell-count{
					}
				}
		
				.goods-price {
					font-size: 26rpx;
					color: #e7612e;
				}
			}
		}
	}

	.goods-main {
		min-height: auto;
		background: #f7f8fa;
		padding-top: 0rpx;

		.header-wrap {
			background: #FFFFFF;
			display: flex;
			padding: 24rpx;
			margin-top: 12rpx;
			border-radius: 8rpx;

			.header-image {
				flex-shrink: 2rpx;
				width: 64rpx;
				height: 64rpx
			}

			.header-item {
				margin: 6rpx auto 0 12rpx;
				font-size: 40rpx;
				font-color: #333333;
				font-weight: bold;
				position: relative;
			}
		}

		.goods-item-wrap {
			background: #FFFFFF;
			display: flex;
			padding: 12rpx;
			margin: 10rpx;
			border-radius: 8rpx;

			.goods-image {
				width: 200rpx;
				height: 200rpx;
				border-radius: 12rpx;
				flex: none;
			}

			.goods-content-wrap {
				margin-top: 12rpx;
				padding-left: 24rpx;

				.goods-name {
					font-size: 35rpx;
					font-weight: bold;
					text-align: center;
				}

				.goods-stock-wrap {
					color: #969696;
					font-size: 26rpx;
					margin-top: 12rpx;

					.goods-stock {
						margin-right: 24rpx;
					}
				}

				.goods-price {
					font-size: 26rpx;
					color: #e7612e;
				}
			}
		}
	}
</style>
