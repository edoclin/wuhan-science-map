<template>
  <el-form :rules="rules" :model="form" label-width="120px" ref="formRef">
    <el-form-item label="名称" prop="tripName">
      <el-input v-model="form['tripName']"/>
    </el-form-item>
    <el-form-item label="背景图" prop="accessKey">
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
            <span v-else>请选择图片文件上传</span>
          </div>
        </template>
      </el-upload>
    </el-form-item>
    <el-form-item label="活动开始时间" prop="startingTime">
      <el-date-picker
          v-model="form['startingTime']"
          type="datetime"
          format="YYYY年MM月DD日 HH时mm分"
          placeholder="请选择智慧行开始时间"
      />
    </el-form-item>
    <el-form-item label="活动等级" prop="displayIndex">
      <el-input v-model="form['displayIndex']" placeholder="0为最高等级"/>
    </el-form-item>
    <el-form-item label="活动简介" prop="descSimple">
      <el-input v-model="form['descSimple']"/>
    </el-form-item>
    <el-form-item label="详细介绍" prop="descRichText">
      <div style="border: 1px solid #ccc">
        <Toolbar style="border-bottom: 1px solid #ccc" :editor="editorRef4Desc" :defaultConfig="toolbarConfig4Desc"
                 mode="default"/>
        <Editor style="height:  calc(100vh - 120px);width: 100%; overflow-y: hidden;" v-model="valueHtml4Desc"
                :defaultConfig="editorConfig4Desc" mode="default" @onCreated="handleCreated4Desc"/>
      </div>
    </el-form-item>

    <el-form-item label="创建人" prop="creator">
      <el-input v-model="form['creator']"/>
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
import { postZhihuiTrip, putZhihuiTrip } from 'src/api/zhihui'
import { ElMessage } from 'element-plus'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'

import { useMapState } from 'src/stores'
import { useCommonStore } from 'src/stores/common_store'
import { openURL } from 'quasar'
import { getAccessUrl, sliceUploadFile } from 'src/api/cos'

const { status } = useMapState(useCommonStore, ['status'])

const bus = inject('bus')

const formRef = ref()


const uploading = ref(false)
const fileList = ref([])

const handlePreviewUpload = (e) => {
  openURL(e.url)
}

const handleChangeFileUpload = (uploadFile) => {
  uploading.value = true
  fileList.value = []
  sliceUploadFile(uploadFile.raw, 'trip-cover').then(data => {
    form['accessKey'] = data['Key']
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
  placeholder: '请输入智慧行详细介绍',
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
const onResetForm = (formEl) => {
  if (formEl) {
    formEl.resetFields()
  }
}

const form = reactive({
  'tripName': '',
  'accessKey': '',
  'startingTime': Date,
  'displayIndex': '',
  'descRichText': '',
  'descSimple': '',
  'creator': ''
})

const rules = reactive({
  'tripName': [
    {
      required: true,
      trigger: 'blur',
      message: '请输入智慧行名称'
    }
  ],
  'accessKey': [
    {
      required: true,
      trigger: 'blur',
      message: '请上传智慧行封面'
    }
  ],
  'startingTime': [
    {
      required: true,
      trigger: 'blur',
      message: '请选择开始时间'
    },
  ],
  'displayIndex':[
    {
      required: true,
      trigger: 'blur',
      message: '请输入等级排序'
    }
  ],
  'descSimple': [
    {
      required: true,
      trigger: 'blur',
      message: '请输入智慧行简介'
    }
  ],
  'descRichText':[
    {
      required: true,
      trigger: 'blur',
      message: '请输入智慧行详细介绍'
    }
  ],
  'creator':[
    {
      required: true,
      trigger: 'blur',
      message: '请输入创建人'
    }
  ]
})

const onSubmit = (formEl) => {
  form['descRichText'] = valueHtml4Desc.value
  formEl.validate(async (valid) => {
    if (valid) {
      if (props.data && JSON.stringify(props.data) !== '{}') {
        await putZhihuiTrip(form).then(res => {
          ElMessage({
            type: 'success',
            message: res.data,
          })
        })
      } else {
        await postZhihuiTrip(form).then(res => {
          ElMessage({
            type: 'success',
            message: res.data,
          })
          onResetForm(formEl)
        })
      }

      valueHtml4Desc.value = ''
      fileList.value = []
      bus.emit('update-zhihui-table')
    }
  })
}

const props = defineProps({
  data: {}
})
if (props.data && JSON.stringify(props.data) !== '{}') {
  console.log(new Date(props.data['startingTimestamp']))
  form['id'] = props.data['id']
  form['tripName'] = props.data['tripName']
  form['displayIndex'] = props.data['displayIndex']
  form['accessKey'] = props.data['accessKey']
  fileList.value = [{
    name: '',
    url: props.data['accessUrl']
  }]
  form['startingTime'] = new Date(props.data['startingTimestamp'])
  form['descSimple'] = props.data['descSimple']
  form['contentRichText'] = props.data['contentRichText']

  valueHtml4Desc.value = props.data['descRichText']

  form['creator'] = props.data['creator']
  form['status'] = status.value.find(item => item.value === props.data['status']).key

}

</script>
