<template>
  <div>
    <el-config-provider :locale="zhCn">
      <el-container>
        <el-aside width="220px" class="disabled-selected">
          <el-menu style="height: calc(100vh - 70px)">
            <el-sub-menu index="activity">
              <template #title>
                <el-icon>
                  <Location/>
                </el-icon>
                <span>营销活动</span>
              </template>
              <el-menu-item-group title="活动">
                <el-menu-item index="activity/table"
                              @click="clickMenuItem('活动列表', 'ActivityTable', 'ActivityTable', './activity/Table')">
                  <el-icon>
                    <Document/>
                  </el-icon>
                  活动列表
                </el-menu-item>
                <el-menu-item index="activity/form"
                              @click="clickMenuItem('新增活动', 'ActivityForm', 'ActivityForm', './activity/Form')">
                  <el-icon>
                    <FolderAdd/>
                  </el-icon>
                  新增活动
                </el-menu-item>
              </el-menu-item-group>
            </el-sub-menu>
            <el-sub-menu index="news">
              <template #title>
                <el-icon>
                  <Location/>
                </el-icon>
                <span>热点资讯</span>
              </template>
              <el-menu-item-group title="资讯">
                <el-menu-item index="news/table"
                              @click="clickMenuItem('资讯列表', 'NewsTable', 'NewsTable', './news/Table')">
                  <el-icon>
                    <Document/>
                  </el-icon>
                  资讯列表
                </el-menu-item>
                <el-menu-item index="news/form"
                              @click="clickMenuItem('发布资讯', 'NewsForm', 'NewsForm', './news/Form')">
                  <el-icon>
                    <FolderAdd/>
                  </el-icon>
                  发布资讯
                </el-menu-item>
                <el-menu-item index="news/list"
                              @click="clickMenuItem('资讯排序', 'NewsList', 'NewsList', './news/List')">
                  <el-icon>
                    <FolderAdd/>
                  </el-icon>
                  资讯排序
                </el-menu-item>
              </el-menu-item-group>
            </el-sub-menu>
            <el-sub-menu index="user">
              <template #title>
                <el-icon>
                  <Location/>
                </el-icon>
                <span>用户管理</span>
              </template>
              <el-menu-item-group title="用户">
                <el-menu-item index="user/table"
                              @click="clickMenuItem('用户列表', 'UserTable', 'UserTable', './user/Table')">
                  <el-icon>
                    <Document/>
                  </el-icon>
                  用户列表
                </el-menu-item>
              </el-menu-item-group>
              <el-menu-item-group title="课程">
                <el-menu-item index="user/register/table"
                              @click="clickMenuItem('报名课程', 'ActivityTable', 'ActivityTable', './user/register-course/Table')">
                  <el-icon>
                    <Document/>
                  </el-icon>
                  活动列表
                </el-menu-item>
                <el-menu-item index="user/display/table"
                              @click="clickMenuItem('成果展示', 'ResultDisplayTable', 'ResultDisplayTable', './user/result-display/Table')">
                  <el-icon>
                    <Document/>
                  </el-icon>
                  成果展示
                </el-menu-item>
              </el-menu-item-group>
            </el-sub-menu>
            <el-sub-menu index="science-base">
              <template #title>
                <el-icon>
                  <Location/>
                </el-icon>
                <span>科普基地</span>
              </template>
              <el-menu-item-group title="基地">
                <el-menu-item index="base/table"
                              @click="clickMenuItem('基地列表', 'BaseTable', 'BaseTable', './base/Table')">
                  <el-icon>
                    <Document/>
                  </el-icon>
                  基地列表
                </el-menu-item>
                <el-menu-item index="base/form"
                              @click="clickMenuItem('新增基地', 'BaseForm', 'BaseForm', './base/Form')">
                  <el-icon>
                    <FolderAdd/>
                  </el-icon>
                  新增基地
                </el-menu-item>
                <el-menu-item index="base/list"
                              @click="clickMenuItem('基地排序', 'BaseList', 'BaseList', './base/List')">
                  <el-icon>
                    <FolderAdd/>
                  </el-icon>
                  基地排序
                </el-menu-item>
              </el-menu-item-group>
              <el-menu-item-group title="课程">
                <el-menu-item index="course/table"
                              @click="clickMenuItem('课程列表', 'CourseTable', 'CourseTable', './course/Table')">
                  <el-icon>
                    <Document/>
                  </el-icon>
                  课程列表
                </el-menu-item>
                <el-menu-item index="course/form"
                              @click="clickMenuItem('新增课程', 'CourseForm', 'CourseForm', './course/Form')">
                  <el-icon>
                    <FolderAdd/>
                  </el-icon>
                  新增课程
                </el-menu-item>
              </el-menu-item-group>
              <el-menu-item-group title="讲师">
                <el-menu-item index="lecturer/table"
                              @click="clickMenuItem('讲师列表', 'LecturerTable', 'LecturerTable', './lecturer/Table')">
                  <el-icon>
                    <Document/>
                  </el-icon>
                  讲师列表
                </el-menu-item>
                <el-menu-item index="lecturer/form"
                              @click="clickMenuItem('新增讲师', 'LecturerForm', 'LecturerForm', './lecturer/Form')">
                  <el-icon>
                    <FolderAdd/>
                  </el-icon>
                  新增讲师
                </el-menu-item>
              </el-menu-item-group>
              <el-menu-item-group title="展厅">
                <el-menu-item index="hall/table"
                              @click="clickMenuItem('展厅列表', 'HallTable', 'HallTable', './hall/Table')">
                  <el-icon>
                    <Document/>
                  </el-icon>
                  展厅列表
                </el-menu-item>
                <el-menu-item index="hall/form"
                              @click="clickMenuItem('新增展厅', 'HallForm', 'HallForm', './hall/Form')">
                  <el-icon>
                    <FolderAdd/>
                  </el-icon>
                  新增展厅
                </el-menu-item>
              </el-menu-item-group>
            </el-sub-menu>
            <el-sub-menu index="link">
              <template #title>
                <el-icon>
                  <Location/>
                </el-icon>
                <span>友情链接</span>
              </template>
              <el-menu-item-group title="链接">
                <el-menu-item index="link/table"
                              @click="clickMenuItem('链接列表', 'LinkTable', 'LinkTable', './link/Table')">
                  <el-icon>
                    <Document/>
                  </el-icon>
                  链接列表
                </el-menu-item>
                <el-menu-item index="link/form"
                              @click="clickMenuItem('新增链接', 'LinkForm', 'LinkForm', './link/Form')">
                  <el-icon>
                    <FolderAdd/>
                  </el-icon>
                  新增链接
                </el-menu-item>
              </el-menu-item-group>
            </el-sub-menu>

            <el-sub-menu index="zhuihuiTrip">
              <template #title>
                <el-icon>
                  <Location/>
                </el-icon>
                <span>智慧行</span>
              </template>
              <el-menu-item-group title="智慧行">
                <el-menu-item index="zhihui/table"
                              @click="clickMenuItem('智慧行列表', 'ZhihuiTable', 'ZhihuiTable', './zhihui/Table')">
                  <el-icon>
                    <Document/>
                  </el-icon>
                  智慧行列表
                </el-menu-item>
                <el-menu-item index="zhihui/form"
                              @click="clickMenuItem('新增智慧行', 'ZhihuiForm', 'ZhihuiForm', './zhihui/Form')">
                  <el-icon>
                    <FolderAdd/>
                  </el-icon>
                  新增智慧行
                </el-menu-item>
              </el-menu-item-group>
              <el-menu-item-group title="智慧行路线">
                <el-menu-item index="zhihuiItem/table"
                              @click="clickMenuItem('智慧行路线列表', 'ZhihuiItemTable', 'ZhihuiItemTable', './zhihuiItem/Table')">
                  <el-icon>
                    <Document/>
                  </el-icon>
                  智慧行路线列表
                </el-menu-item>
                <el-menu-item index="zhihuiItem/form"
                              @click="clickMenuItem('新增智慧行路线', 'ZhihuiItemForm', 'ZhihuiItemForm', './zhihuiItem/Form')">
                  <el-icon>
                    <FolderAdd/>
                  </el-icon>
                  新增智慧行路线
                </el-menu-item>
              </el-menu-item-group>

            </el-sub-menu>
          </el-menu>
        </el-aside>
        <el-container>
          <el-header style="-webkit-app-region: drag">
            <el-menu
                default-active="user"
                mode="horizontal"
                :ellipsis="false">
              <div style="font-size: 3ch; margin-left: 20px; margin-top: 10px;user-select: none">
                武汉科普一张图
              </div>
              <div style="margin-left: 10px; margin-top: 21px;color: #d6d6d7;" class="disabled-selected">
                后台管理系统
              </div>
              <div class="flex-grow"/>
              <el-button size="large" style="margin-top: 5px;-webkit-app-region: no-drag" text
                         :icon="isFullscreen ? SvgExitFullScreen : FullScreen" @click="onToggle($event)"></el-button>
              <div>
                <el-switch
                    style="margin-top: 8px;-webkit-app-region: no-drag"
                    v-model="isDark"
                    inline-prompt
                    :active-icon="Moon"
                    :inactive-icon="Sunny"
                    active-color="#2c2c2c"/>
              </div>
              <el-badge v-if="false" :value="99" style="margin-top: 20px;margin-right: 15px;margin-left: 18px">
                <el-icon :size="17">
                  <ChatDotSquare/>
                </el-icon>
              </el-badge>
              <el-sub-menu index="setting" class="disabled-selected" style="-webkit-app-region: no-drag">
                <template #title>{{ userInfo.realName }}</template>
                <el-menu-item index="logout" @click="logout" class="disabled-selected">退出登录</el-menu-item>
              </el-sub-menu>
            </el-menu>
          </el-header>
          <el-main>
            <el-tabs
                class="disabled-selected"
                style="height: calc(100vh - 100px - 12px)"
                v-model="currentTab.name"
                type="card"
                closable
                @tab-remove="removeTab">
              <el-tab-pane
                  v-for="item in editableTabs"
                  :key="item.key"
                  :label="item.title"
                  :name="item.name">
                <component style="user-select: text"
                           :is="item.component" :data="children[item.name]"></component>
              </el-tab-pane>
            </el-tabs>
            <el-footer class="disabled-selected">
              © 2023 武汉图歌信息技术有限责任公司
            </el-footer>
          </el-main>
        </el-container>
      </el-container>
    </el-config-provider>
  </div>
</template>
<script setup>
import { mapActions } from 'pinia'
import { Moon, Sunny, FullScreen } from '@element-plus/icons-vue'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import { useDark, useToggle } from '@vueuse/core'
import screenfull from 'screenfull'
import { useCommonStore } from 'stores/common_store'
import { webLogout } from 'src/api/user'
import { useRouter } from 'vue-router'
import { useUserStore } from 'src/stores/user_store'
import { useMapState } from 'src/stores'
import { listStatus } from 'src/api/common'
import SvgExitFullScreen from 'components/SvgExitFullScreen.vue'

const {
  userInfo
} = useMapState(useUserStore, ['userInfo'])

const pageModules = import.meta.glob('./**/**/**/**/**.vue')
const bus = inject('bus')
const commonActions = mapActions(useCommonStore,
    [
      'updateRoles',
      'updateStatus',
      'updateBaseTypes'])

const isFullscreen = ref(false)

const change = () => {
  isFullscreen.value = screenfull.isFullscreen
}

const onBlur = (evt) => {
  let target = evt.target
  if (target.nodeName === 'svg') {
    target = evt.target.parentNode.parentNode
  }
  target.blur()
  return true
}

const onToggle = (evt) => {
  onBlur(evt)
  screenfull.toggle()
}

const router = useRouter()
const userAction = mapActions(useUserStore,
    [
      'updateToken',
      'updateUserInfo'])

const localStorage = inject('localStorage')

const logout = () => {
  webLogout().then(res => {
    userAction.updateToken({})
    userAction.updateUserInfo({})
    localStorage.remove('token')
    localStorage.remove('userInfo')
    router.push({
      path: '/cms/login'
    })
  })
}

onMounted(() => {
  screenfull.on('change', change)
  listStatus().then(res => {
    commonActions.updateStatus(res.data)
  })
})

onUnmounted(() => {
  screenfull.off('change', change)
})

const isDark = useDark()

const toggleDark = useToggle(isDark)

const editableTabs = reactive([])
let currentTab = reactive({
  name: ''
})

let data = reactive({
  isCollapse: false
})

const clickMenuItem = (title, name, key, componentPath) => {
  if (editableTabs.find(item => item.name === name) !== undefined) {
    currentTab.name = name
  } else {
    pageModules[`${componentPath}.vue`]().then(item => {
      editableTabs.push({
        component: markRaw(item.default),
        title: title,
        name: name,
        key: key
      })
      currentTab.name = name
    })
  }
}

const removeTab = (name) => {
  console.log(name)
  let index = editableTabs.findIndex(item => item.name === name)
  // chrome 标签
  if (index === 0 && editableTabs.length === 1) {
    // todo: 打开引导页
  } else if (index === editableTabs.length - 1) {
    //最后一个标签 -> 打开前一个标签
    currentTab.name = editableTabs[index - 1].name
  } else {
    // 打开后一个标签
    currentTab.name = editableTabs[index + 1].name
  }
  editableTabs.splice(index, 1)
  delete children[name]
}

const children = reactive({})

bus.on('edit-item', ({
  record,
  component,
  title,
  name
}) => {
  if (editableTabs.find(item => item.name === name) !== undefined) {
    currentTab.name = name
  } else {
    editableTabs.push({
      component: markRaw(component),
      title: title,
      name: name,
      key: component.__name
    })
    children[name] = record
    currentTab.name = name
  }
})
</script>
<style>
.flex-grow {
  flex-grow: 1;
}

.el-container {
  height: 100%;
}

.el-header {
  margin-top: 10px;
  position: relative;
  width: 100%;
  height: 60px;
}

.el-footer {
  position: fixed;
  right: 3%;
  font-size: 12px;
  height: 12px;
  color: #676a6c;
}

.el-aside {
  display: block;
  position: absolute;
  left: 0;
  top: 70px;
  bottom: 0;
}

.el-main {
  position: absolute;
  left: 200px;
  right: 0;
  top: 70px;
  bottom: 0;
}

.disabled-selected {
  user-select: none
}
</style>
