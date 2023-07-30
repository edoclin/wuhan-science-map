<template>
  <el-form :rules="rules" :model="form" label-width="150px" ref="formRef">
    <el-form-item label="课程名称" prop="courseName">
      <el-input v-model="form['courseName']"/>
    </el-form-item>
    <el-form-item label="所属基地" prop="baseId">
      <el-select v-model="form['baseId']" placeholder="请选择所属基地" style="width: 100%">
        <el-button :icon="Edit" text @click="addNewBase">新增基地</el-button>
        <el-option v-for="base in baseSelector" :key="base.id" :label="base.baseName"
                   :value="base.id"/>
      </el-select>
    </el-form-item>
    <el-form-item label="所属区域" prop="regionCode">
      <el-cascader v-model="form['regionCode']" :options="regions" @change="handleChangeRegion"
                   :props="{label:'regionName', value: 'code'}">
        <template #default="{ node, data }">
          <span>{{ data['regionName'] }}</span>
          <span v-if="!node.isLeaf"> ({{ data['children'].length }}) </span>
        </template>
      </el-cascader>
    </el-form-item>
    <el-form-item label="详细地址" prop="detailAddress">
      <el-input v-model="form['detailAddress']"/>
    </el-form-item>
    <el-form-item label="开课时间" prop="startingTime">
      <el-date-picker
          v-model="form['startingTime']"
          type="datetime"
          format="YYYY年MM月DD日 HH时mm分"
          placeholder="请选择开课日期和时间"
      />
    </el-form-item>
    <el-form-item label="适宜年龄" prop="targetAge">
      <el-input-number v-model="form['targetAge']" :min="1" :max="150" style="margin-right: 15px"/>
      岁以上
    </el-form-item>
    <el-form-item label="轮播图展示">
      <el-radio-group v-model="form['top']">
        <el-radio-button :label="true">是</el-radio-button>
        <el-radio-button :label="false">否</el-radio-button>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="精彩课程推荐">
      <el-radio-group v-model="form['wonderful']">
        <el-radio-button :label="true">是</el-radio-button>
        <el-radio-button :label="false">否</el-radio-button>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="精彩课程展示图片" prop="wonderfulImageAccessKey">
      <el-upload :on-preview="handlePreviewUpload" list-type="picture-card" v-model:file-list="fileList"
                 :auto-upload="false" accept="image/*"
                 :on-change="handleChangeFileUpload">
        <el-button type="primary" :loading="uploading">
          {{ uploading ? '上传中...' : (fileList.length === 1 ? '重新上传' : '点击上传') }}
        </el-button>
        <template #tip>
          <div class="el-upload__tip">
            <el-progress
                style="width: 202px"
                v-if="uploading"
                :percentage="100"
                :indeterminate="true"
                :duration="1"
                :format="() => ''"
            />
            <span v-else>若精彩课程推荐设为是请务必选择图片文件上传, 否则小程序端无法正常显示图片!</span>
          </div>
        </template>
      </el-upload>
    </el-form-item>

    <el-form-item label="课程内容" prop="descRichText">
      <div style="border: 1px solid #ccc">
        <Toolbar style="border-bottom: 1px solid #ccc" :editor="editorRef4Desc" :defaultConfig="toolbarConfig4Desc"
                 mode="default"/>
        <Editor style="height:  calc(100vh - 120px);width: 100%; overflow-y: hidden;" v-model="valueHtml4Desc"
                :defaultConfig="editorConfig4Desc" mode="default" @onCreated="handleCreated4Desc"/>
      </div>
    </el-form-item>
    <el-form-item label="当前状态">
      <el-select v-model="form['status']" placeholder="请选择状态" style="width: 100%">
        <el-option v-for="status in status" :key="status.key" :label="status.value"
                   :value="status.key"/>
      </el-select>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="onSubmit(formRef)">{{ data ? '更新' : '创建' }}</el-button>
      <el-button v-if="!data" @click="onResetForm(formRef)">重置</el-button>
    </el-form-item>
  </el-form>
</template>
<script setup>
import '@wangeditor/editor/dist/css/style.css'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import {Edit} from '@element-plus/icons-vue'

import { postBaseCourse, putBaseCourse } from 'src/api/course'
import { ElMessage } from 'element-plus'

import { useMapState } from 'src/stores'
import { useCommonStore } from 'src/stores/common_store'
import { scienceBaseSelector } from 'src/api/science-base'
import { getRegionCentroid, listAllRegions } from 'src/api/region'
import { regeo } from 'src/api/amap'
import { getAccessUrl, sliceUploadFile } from 'src/api/cos'
import BaseFormVue from 'pages/cms/base/Form.vue'
import { openURL } from 'quasar'

const { status } = useMapState(useCommonStore, ['status'])

const bus = inject('bus')

const formRef = ref()

const addNewBase = () => {
  bus.emit('edit-item', {
    record: {},
    component: BaseFormVue,
    title: '新增基地',
    name: Math.random()
  })
}


const uploading = ref(false)
const fileList = ref([])

const handlePreviewUpload = (e) => {
  openURL(e.url)
}

const handleChangeFileUpload = (uploadFile) => {
  uploading.value = true
  fileList.value = []
  sliceUploadFile(uploadFile.raw, 'wonderful-image').then(data => {
    form['wonderfulImageAccessKey'] = data['Key']
    ElMessage({
      type: 'success',
      message: '上传成功'
    })
    uploading.value = false
    getAccessUrl(data['Key']).then(res => {
      fileList.value.push({
        name: '',
        url: res.data
      })
    })
  }).catch(err => {
    uploading.value = false
    ElMessage({
      type: 'error',
      message: '服务器繁忙,请重试!'
    })
  })
}

const editorRef4Desc = shallowRef()
const valueHtml4Desc = ref('')

onBeforeUnmount(() => {
  let editor = editorRef4Desc.value
  if (editor != null) editor.destroy()
})

const handleCreated4Desc = (editor) => {
  editorRef4Desc.value = editor
}

const toolbarConfig4Desc = reactive({
  excludeKeys: [
    'fullScreen'
  ]
})

const editorConfig4Desc = reactive({
  placeholder: '请输入课程内容',
  MENU_CONF: {
    'uploadImage': {
      maxFileSize: 1024 * 1024 * 10,
      maxNumberOfFiles: 100,
      allowedFileTypes: ['image/*'],
      metaWithUrl: false,
      withCredentials: true,
      timeout: 10 * 1000,
      async customUpload (file, insertFn) {
        sliceUploadFile(file, 'wuhan-science-rich-text').then(res => {
          insertFn(`https://${res.Location}`, '', `https://${res.Location}`)
        }).catch(err => {
          ElMessage({
            type: 'error',
            message: '服务器繁忙,请重试!'
          })
        })
      }
    },
    'uploadVideo': {
      maxFileSize: 1024 * 1024 * 100 * 10,
      maxNumberOfFiles: 10,
      allowedFileTypes: ['video/*', 'audio/*'],
      metaWithUrl: false,
      withCredentials: true,
      timeout: 20 * 1000,
      async customUpload (file, insertFn) {
        sliceUploadFile(file, 'wuhan-science-rich-text').then(res => {
          insertFn(`https://${res.Location}`, '', `https://${res.Location}`)
        }).catch(err => {
          ElMessage({
            type: 'error',
            message: '服务器繁忙,请重试!'
          })
        })
      }
    }
  }
})

const regions = ref([])

listAllRegions().then(res => {
  regions.value = res.data
})
const handleChangeRegion = (e) => {
  form['regionCode'] = e[e.length - 1]
  getRegionCentroid(form['regionCode']).then(res => {
    regeo(res['data']['lng'], res['data']['lat']).then(resp => {
      form['detailAddress'] = resp
    })
  })
}
const baseSelector = ref([])



bus.on('update-base-selector', () => {
  scienceBaseSelector().then(res => {
    baseSelector.value = res.data
  })
})
scienceBaseSelector().then(res => {
  baseSelector.value = res.data
})

const form = reactive({
  'courseName': '',
  'baseId': '',
  'regionCode': '',
  'detailAddress': '',
  'startingTime': Date,
  'descRichText': '',
  'targetAge': 3,
  'top': false,
  'wonderful': false,
  'wonderfulImageAccessKey': ''
})

const rules = reactive({
  'courseName': [
    {
      required: true,
      trigger: 'blur',
      message: '请输入课程名称'
    },
  ],
  'baseId': [
    {
      required: true,
      trigger: 'blur',
      message: '请选择所属基地'
    },
  ],
  'regionCode': [
    {
      required: true,
      trigger: 'blur',
      message: '请选择所属区域'
    },
  ],
  'detailAddress': [
    {
      required: true,
      trigger: 'blur',
      message: '请输入详细地址'
    },
  ],
  'startingTime': [
    {
      required: true,
      trigger: 'blur',
      message: '请选择开课时间'
    },
  ],
  'descRichText': [
    {
      required: true,
      trigger: 'blur',
      message: '请输入课程内容'
    },
  ],
  'wonderfulImageAccessKey': [
    {
      required: form['wonderful'],
      trigger: 'blur',
      message: '请上传精彩课程展示图片'
    },
  ],
})
const onResetForm = (formEl) => {
  if (formEl) {
    formEl.resetFields()
  }
}

const onSubmit = (formEl) => {
  form['descRichText'] = valueHtml4Desc.value
  const len = form['regionCode'].length
  if (len !== 1 && typeof form['regionCode'] !== 'number') {
    console.log(len)
    form['regionCode'] = form['regionCode'][len - 1]
  }
  formEl.validate(async (valid) => {
    if (valid) {
      if (props.data && JSON.stringify(props.data) !== '{}') {
        await putBaseCourse(form).then(res => {
          ElMessage({
            type: 'success',
            message: res.data,
          })
        })
      } else {
        await postBaseCourse(form).then(res => {
          ElMessage({
            type: 'success',
            message: res.data,
          })
          onResetForm(formEl)
        })
      }
      valueHtml4Desc.value = ""
      bus.emit('update-course-table')
    }
  })
}

const props = defineProps({
  data: {}
})
if (props.data && JSON.stringify(props.data) !== '{}') {
  console.log(props)
  console.log(new Date(props.data['startingTimestamp']))
  form['id'] = props.data['id']
  form['regionCode'] = props.data['regionCodes']
  form['baseName'] = props.data['baseName']
  form['baseId'] = props.data['baseId']
  form['courseName'] = props.data['courseName']
  form['detailAddress'] = props.data['detailAddress']
  form['top'] = props.data['top']
  form['wonderful'] = props.data['wonderful']
  form['targetAge'] = props.data['targetAge']
  form['startingTime'] = new Date(props.data['startingTimestamp'])
  form['wonderfulImageAccessKey'] = props.data['wonderfulImageAccessKey']
  valueHtml4Desc.value = props.data['descRichText']

  form['status'] = status.value.find(item => item.value === props.data['status']).key

  fileList.value = [{
    name: '',
    url: props.data['wonderfulImageAccessKeyUrl']
  }]
}

</script>
