<template>
	<view>
		<!-- 智慧行 -->
		<view class="travel-main">
			<view class="header-wrap">
				<image class="header-image" src="../../static/HM-PersonalCenter/sever/addr.png"></image>
				<view class="header-item">智慧行</view>
			</view>
			
			<view>
				<u-skeleton :loading="skeleton" :animate="true" :rows="20" :title="false">
					<u-empty v-if="zhihuiTripList.length === 0" mode="list"
						icon="http://cdn.uviewui.com/uview/empty/list.png" text="来早了...">
					</u-empty>
					<view v-else>
						<view class="goods-item-wrap" @click="toTripDetail(item.id,item.tripName)"
							v-for="(item, index) in zhihuiTripList" :key="index">
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
	</view>
</template>

<script setup>
import { ref } from "vue";
import {
		zhihuiTrip
	} from "../../api/trip"
const zhihuiTripList = ref([])
const skeleton = ref(false)

zhihuiTrip(0).then(res =>{
	console.log(res.data)
	setTimeout(() => {
		skeleton.value = false
	}, 300)
	zhihuiTripList.value = res.data
})

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
				}
		
			}
		}
	}
</style>
