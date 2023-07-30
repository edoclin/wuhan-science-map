<template>
	<view>
		<view v-if="userStore['openid'] === ''">
			<view class="header">
				<view class="userinfo">
					<view class="face" open-type="getPhoneNumber" @getphonenumber="login" :loading="loginLoading">
						<image src="http://pic.imeitou.com/uploads/allimg/230327/7-23032G10328.jpg">
						</image>
					</view>
					<view class="info" open-type="getPhoneNumber" @getphonenumber="login" :loading="loginLoading">
						<view class="username">未登录,请登陆</view>
					</view>
				</view>
			</view>
		</view>
		<view v-else>
			<view class="header">
				<view class="userinfo">
					<view class="face">
						<image src="http://pic.imeitou.com/uploads/allimg/230327/7-23032G10321.jpg">
						</image>
					</view>
					<view class="info">
						<view class="username">{{userStore.userInfo['realName']}}</view>
					</view>
				</view>
				<view class="setting" @click="toOtherPage('setting')">
					<image src="../../static/HM-PersonalCenter/setting.png" @click="toOtherPage('setting')"></image>
				</view>
			</view>
		</view>
		<view class="orders">
			<view class="box">
				<view class="label" v-for="(row,index) in orderTypeLise" :key="row.name" hover-class="hover"
					@click="toOtherPage(row.pageName,row.param)">
					<view class="icon">
						<image :src="'../../static/HM-PersonalCenter/'+row.icon"></image>
					</view>
					{{row.name}}
				</view>
			</view>
		</view>
		<view class="list" v-for="(list,list_i) in severList" :key="list_i">
			<view class="li" v-for="(li,li_i) in list"
				v-bind:class="{'noborder':li_i==list.length-1}" hover-class="hover" :key="li.name">
				<view class="icon">
					<image :src="'../../static/HM-PersonalCenter/sever/'+li.icon"></image>
				</view>
				<view class="text" @click="toOtherPage(li?.pageName,li?.param)">{{li.name}}</view>
				<image class="to" src="../../static/HM-PersonalCenter/to.png"></image>
			</view>
		</view>
	</view>
	<view v-if="userStore['openid'] === ''">
		<u-button shape="circle" icon="https://res.wx.qq.com/a/wx_fed/assets/res/NTI4MWU5.ico" text="微信登录"
			type="success" open-type="getPhoneNumber" @getphonenumber="login" :loading="loginLoading">
		</u-button>
	</view>
</template>
<script setup>
	import {
		decryptMobileData,
		loginApp,
		logoutApp
	} from '../../api/user';
	import {
		useUserStore
	} from '../../stores/user_store';
	import {
		reactive,
		ref
	} from "vue";

	const userStore = useUserStore();
	const orderTypeLise = reactive([{
		name: '报名课程',
		icon: 'l1.png',
		pageName: 'user-course',
		param: 0
	}, {
		name: '收藏课程',
		icon: 'l2.png',
		pageName: 'user-course',
		param: 1
	}, {
		name: '报名活动',
		icon: 'l6.png',
		pageName: 'course-upload-result',
		param: -1
	}, {
		name: '成果展示',
		icon: 'l3.png',
		pageName: 'course-upload-result',
		param: -1
	}
	]);
	const severList = reactive([
		[{
				name: '报名课程',
				icon: 'quan.png',
				pageName: 'user-course',
				param: 0
			}, {
				name: '收藏课程',
				icon: 'point.png',
				pageName: 'user-course',
				param: 1
			},{
				name: '报名活动',
				icon: 'point.png',
				pageName: 'user-course',
				param: 1
			},{
				name: '收藏活动',
				icon: 'point.png',
				pageName: 'user-course',
				param: 1
			},
			{
				name: '成果展示',
				icon: 'momey.png',
				pageName: 'course-upload-result',
				param: -1
			}
		],
		[{
			name: '设置',
			icon: 'security.png',
			pageName: 'setting',
		}]
	]);
	const loginLoading = ref(false)
	const hoverClass = ref('hvr-float')

	uni.$on('getphonenumber-listener', (detail) => {
		decryptMobileData(detail).then(({
			data
		}) => {
			userStore.updateMobile(data.phoneNumber)
		}).catch((err) => {
			decryptMobileData(detail).then(({
				data
			}) => {
				userStore.updateMobile(data.phoneNumber)
			})
		})
	})

	const login = ({
		detail
	}) => {
		loginLoading.value = true
		uni.login({
			provider: 'weixin',
			success({
				code
			}) {
				loginApp(code).then((res) => {
					loginLoading.value = false
					userStore.updateToken(res.data['token'])
					if (res.code === 5102) {
						uni.showToast({
							icon: 'loading',
							title: '即将跳转至注册',
							mask: true,
							duration: 1500
						})
						setTimeout(() => {
							uni.navigateTo({
								url: '/pages/user-register/user-register'
							})
						}, 1500)
					} else {
						userStore.updateOpenid(res.data['openid'])
						res.data['isMale'] = res.data['parentIsMale']
						userStore.updateUserInfo(res.data)
					}
					setTimeout(() => {
						uni.$emit('getphonenumber-listener', detail)
					}, 1000 * 15)
				})
			}
		})
	}
	const toOtherPage = (pageName, param) => {
		if (userStore.openid === '') {
			uni.showToast({
				icon: "error",
				title: '请先登录'
			})
			return
		}

		setTimeout(() => {
			uni.navigateTo({
				url: `/pages/${pageName}/${pageName}?tab=${param}&id=${param}`
			})
		}, 250)

	}
</script>

<style lang="scss">
	page {
		background-color: #fff
	}

	.header {
		&.status {
			padding-top: var(--status-bar-height);
		}

		background-color:#4fa0d7;
		width:92%;
		height:30vw;
		padding:0 4%;
		display:flex;
		align-items:center;

		.userinfo {
			width: 90%;
			display: flex;

			.face {
				flex-shrink: 0;
				width: 15vw;
				height: 15vw;

				image {
					margin-left: 4vw;
					width: 100%;
					height: 100%;
					border-radius: 100%
				}
			}

			.info {
				display: flex;
				flex-flow: wrap;
				padding-left: 30upx;

				.username {
					width: 100%;
					color: #fff;
					font-size: 40upx;
					margin: 4vw 0 0 8vw;
				}

				.integral {
					display: flex;
					align-items: center;
					padding: 0 20upx;
					height: 40upx;
					color: #fff;
					background-color: rgba(0, 0, 0, 0.1);
					border-radius: 20upx;
					font-size: 24upx
				}
			}
		}

		.setting {
			flex-shrink: 0;
			width: 6vw;
			height: 6vw;

			image {
				width: 100%;
				height: 100%
			}
		}
	}

	.hover {
		background-color: #eee
	}

	.orders {
		background-color: #4fa0d7;
		width: 92%;
		height: 11vw;
		padding: 0 4%;
		margin-bottom: calc(11vw + 40upx);
		display: flex;
		align-items: flex-start;
		border-radius: 0 0 100% 100%;
		margin-top: -1upx;

		.box {
			width: 98%;
			padding: 0 1%;
			height: 22vw;
			background-color: #fefefe;
			border-radius: 24upx;
			box-shadow: 0 0 20upx rgba(0, 0, 0, 0.15);
			margin-bottom: 40upx;
			display: flex;
			align-items: center;
			justify-content: center;

			.label {
				display: flex;
				align-items: center;
				justify-content: center;
				flex-flow: wrap;
				width: 100%;
				height: 16vw;
				color: #666666;
				font-size: 26upx;

				.icon {
					position: relative;
					width: 7vw;
					height: 7vw;
					margin: 0 1vw;

					image {
						width: 7vw;
						height: 7vw;
						z-index: 9;
					}
				}
			}
		}
	}

	.list {
		width: 100%;
		border-bottom: solid 26upx #f1f1f1;

		.li {
			width: 92%;
			height: 100upx;
			padding: 0 4%;
			border-bottom: solid 1upx #e7e7e7;
			display: flex;
			align-items: center;

			&.noborder {
				border-bottom: 0
			}

			.icon {
				flex-shrink: 0;
				width: 50upx;
				height: 50upx;

				image {
					width: 50upx;
					height: 50upx
				}
			}

			.text {
				padding-left: 20upx;
				width: 100%;
				color: #666
			}

			.to {
				flex-shrink: 0;
				width: 40upx;
				height: 40upx
			}
		}
	}

	.hvr-float {
		display: inline-block;
		vertical-align: middle;
		-webkit-transform: perspective(1px) translateZ(0);
		transform: perspective(1px) translateZ(0);
		box-shadow: 0 0 1px rgba(0, 0, 0, 0);
		-webkit-transition-duration: 0.3s;
		transition-duration: 0.3s;
		-webkit-transition-property: transform;
		transition-property: transform;
		-webkit-transition-timing-function: ease-out;
		transition-timing-function: ease-out;
	}

	.hvr-float:hover,
	.hvr-float:focus,
	.hvr-float:active {
		-webkit-transform: translateY(-8px);
		transform: translateY(-8px);
	}
</style>
