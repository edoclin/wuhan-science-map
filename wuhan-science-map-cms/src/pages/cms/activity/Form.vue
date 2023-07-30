<template>
  <el-form :rules="rules" :model="form" label-width="120px" ref="formRef">
    <el-form-item label="活动名称" prop="activityName">
      <el-input v-model="form.activityName"/>
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
    <el-form-item label="所属基地" prop="baseId">
      <el-select v-model="form['baseId']" placeholder="请选择所属基地" style="width: 100%">
        <el-option v-for="base in baseSelector" :key="base.id" :label="base.baseName"
                   :value="base.id"/>
      </el-select>
    </el-form-item>
    <el-form-item label="活动详情" prop="descRichText">
      <div style="border: 1px solid #ccc">
        <Toolbar style="border-bottom: 1px solid #ccc" :editor="editorRef" :defaultConfig="toolbarConfig"
                 mode="default"/>
        <Editor style="height:  calc(100vh - 120px);width: 100%; overflow-y: hidden;" v-model="valueHtml"
                :defaultConfig="editorConfig" mode="default" @onCreated="handleCreated"/>
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
import { ElMessage } from 'element-plus'
import { useMapState } from 'src/stores'
import { useCommonStore } from 'stores/common_store'
import { openURL } from 'quasar'
import { getAccessUrl, sliceUploadFile } from 'src/api/cos'
import '@wangeditor/editor/dist/css/style.css'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import { postActivity, putActivity } from 'src/api/activity'
import { scienceBaseSelector } from 'src/api/science-base'

const { status } = useMapState(useCommonStore, ['status'])
const bus = inject('bus')
const formRef = ref(null)

const editorRef = shallowRef()
const valueHtml = ref('')

onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})

const handleCreated = (editor) => {
  editorRef.value = editor
}

const toolbarConfig = reactive({
  excludeKeys: [
    'fullScreen'
  ]
})
const editorConfig = reactive({
  placeholder: '请输入活动详情',
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
      maxFileSize: 1024 * 1024 * 100 * 10, // 100M
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
  sliceUploadFile(uploadFile.raw, 'activity-cover').then(data => {
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
  activityName: '',
  coverAccessKey: '',
  baseId: '',
  descRichText: '',
})

const baseSelector = ref([])

scienceBaseSelector().then(res => {
  baseSelector.value = res.data
})

const rules = reactive({
  activityName: [
    {
      required: true,
      trigger: 'blur',
      message: '请输入活动名称'
    },
  ],
  coverAccessKey: [
    {
      required: true,
      trigger: 'blur',
      message: '请上传活动封面'
    },
  ],
  baseId: [
    {
      required: true,
      trigger: 'blur',
      message: '请选择所属基地'
    },
  ],
  descRichText: [
    {
      required: true,
      trigger: 'blur',
      message: '请输入活动内容'
    },
  ],
})

const onResetForm = (formEl) => {
  if (formEl) {
    formEl.resetFields()
  }
}

const onSubmit = (formEl) => {
  form['descRichText'] = valueHtml.value
  formEl.validate(async (valid) => {
    if (valid) {
      if (props.data) {
        await putActivity(form).then(res => {
          ElMessage({
            type: 'success',
            message: res.data,
          })
        })
      } else {
        await postActivity(form).then(res => {
          ElMessage({
            type: 'success',
            message: res.data,
          })
          onResetForm(formEl)
        })
      }
      valueHtml.value = ''
      fileList.value = []
      bus.emit('update-activity-table')
    }
  })
}

const props = defineProps({
  data: {}
})
if (props.data) {
  console.log(props.data)
  form['id'] = props.data['id']
  form['activityName'] = props.data['activityName']
  form['baseId'] = props.data['baseId']
  form['coverAccessKey'] = props.data['coverAccessKey']

  fileList.value = [{
    name: '',
    url: props.data['coverAccessKeyUrl']
  }]

  form['descRichText'] = props.data['descRichText']
  valueHtml.value = props.data['descRichText']
  form['status'] = status.value.find(item => item.value === props.data['status']).key
}
</script>
