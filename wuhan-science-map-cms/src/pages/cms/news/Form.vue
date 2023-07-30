<template>
  <el-form :rules="rules" :model="form" label-width="120px" ref="formRef">
    <el-form-item label="资讯标题" prop="newsTitle">
      <el-input v-model="form['newsTitle']"/>
    </el-form-item>
    <el-form-item label="撰稿人" prop="creator">
      <el-input v-model="form['creator']"/>
    </el-form-item>
    <el-form-item label="封面图片" prop="coverAccessKey">
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
    <el-form-item label="新闻类型">
      <el-radio-group v-model="form['newsType']">
        <el-radio-button v-for="(item, index) in newsTypes" :key="index" :label="item">{{ item }}</el-radio-button>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="置顶展示">
      <el-radio-group v-model="form['top']">
        <el-radio-button :label="true">是</el-radio-button>
        <el-radio-button :label="false">否</el-radio-button>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="轮播图展示">
      <el-radio-group v-model="form['carousel']">
        <el-radio-button :label="true">是</el-radio-button>
        <el-radio-button :label="false">否</el-radio-button>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="资讯简介" prop="descSimple">
      <el-input v-model="form['descSimple']"/>
    </el-form-item>
    <el-form-item label="资讯正文" prop="contentRichText">
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
import { postNews, putNews } from 'src/api/news'
import { ElMessage } from 'element-plus'

import { useMapState } from 'src/stores'
import { useCommonStore } from 'src/stores/common_store'
import { getAccessUrl, sliceUploadFile } from 'src/api/cos'
import { openURL } from 'quasar'
import { listNewsTypes } from 'src/api/common'

const { status } = useMapState(useCommonStore, ['status'])

const bus = inject('bus')

const newsTypes = ref([])

listNewsTypes().then(res => {
  newsTypes.value = res.data
})

const formRef = ref()

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
  placeholder: '请输入资讯正文',
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

const uploading = ref(false)
const fileList = ref([])

const handlePreviewUpload = (e) => {
  openURL(e.url)
}

const handleChangeFileUpload = (uploadFile) => {
  uploading.value = true
  fileList.value = []
  sliceUploadFile(uploadFile.raw, 'news-cover').then(data => {
    form['coverAccessKey'] = data['Key']
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

const form = reactive({
  'newsTitle': '',
  'newsType': '',
  'coverAccessKey': '',
  'descSimple': '',
  'contentRichText': '',
  'creator': '',
  'top': false,
  'carousel': false,
})

const rules = reactive({
  'newsTitle': [
    {
      required: true,
      trigger: 'blur',
      message: '请输入资讯标题'
    },
  ],
  'newsType': [
    {
      required: true,
      trigger: 'blur',
      message: '请选择新闻类型'
    },
  ],
  'coverAccessKey': [
    {
      required: true,
      trigger: 'blur',
      message: '请上传封面图片'
    },
  ],
  'descSimple': [
    {
      required: true,
      trigger: 'blur',
      message: '请输入资讯简介'
    },
  ],
  'creator': [
    {
      required: true,
      trigger: 'blur',
      message: '请输入撰稿人'
    },
  ],
  'contentRichText': [
    {
      required: true,
      trigger: 'blur',
      message: '请输入资讯正文'
    },
  ],
})
const onResetForm = (formEl) => {
  if (formEl) {
    formEl.resetFields()
  }
}

const onSubmit = (formEl) => {

  form['contentRichText'] = valueHtml4Desc.value
  formEl.validate(async (valid) => {
    if (valid) {
      if (props.data && JSON.stringify(props.data) !== '{}') {
        await putNews(form).then(res => {
          ElMessage({
            type: 'success',
            message: res.data,
          })
        })
      } else {
        await postNews(form).then(res => {
          ElMessage({
            type: 'success',
            message: res.data,
          })
          onResetForm(formEl)
        })
      }

      valueHtml4Desc.value = ''
      fileList.value = []
      bus.emit('update-news-table')
      bus.emit('update-news-list')
    }
  })
}

const props = defineProps({
  data: {}
})
if (props.data && JSON.stringify(props.data) !== '{}') {
  form['status'] = status.value.find(item => item.value === props.data['status']).key
  form['id'] = props.data['id']
  form['newsTitle'] = props.data['newsTitle']
  form['coverAccessKey'] = props.data['coverAccessKey']
  fileList.value = [{
    name: '',
    url: props.data['coverAccessKeyUrl']
  }]
  form['descSimple'] = props.data['descSimple']
  form['contentRichText'] = props.data['contentRichText']

  valueHtml4Desc.value = props.data['contentRichText']
  form['top'] = props.data['top']
  form['carousel'] = props.data['carousel']
  form['newsType'] = props.data['newsType']
  form['creator'] = props.data['creator']
}

</script>
