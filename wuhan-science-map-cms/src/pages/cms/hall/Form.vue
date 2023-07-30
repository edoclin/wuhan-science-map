<template>
  <el-form :rules="rules" :model="form" label-width="120px" ref="formRef">
    <el-form-item label="展厅名称" prop="hallName">
      <el-input v-model="form['hallName']"/>
    </el-form-item>
    <el-form-item label="所属基地" prop="baseId">
      <el-select v-model="form['baseId']" placeholder="请选择所属基地" style="width: 100%">
        <el-button :icon="Edit" text @click="addNewBase">新增基地</el-button>
        <el-option v-for="base in baseSelector" :key="base.id" :label="base.baseName"
                   :value="base.id"/>
      </el-select>
    </el-form-item>'
    <el-form-item label="展厅Logo" prop="hallLogo">
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
    <el-form-item label="展厅简介" prop="descSimple">
      <el-input v-model="form['descSimple']"/>
    </el-form-item>

    <el-form-item label="展厅内容" prop="descRichText">
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
import { Edit } from '@element-plus/icons-vue'
import { postBaseHall, putBaseHall } from 'src/api/hall'
import { ElMessage } from 'element-plus'

import { useMapState } from 'src/stores'
import { useCommonStore } from 'src/stores/common_store'
import BaseFormVue from 'pages/cms/base/Form.vue'
import { getAccessUrl, sliceUploadFile } from 'src/api/cos'
import { scienceBaseSelector } from 'src/api/science-base'
import { openURL } from 'quasar'

const { status } = useMapState(useCommonStore, ['status'])

const bus = inject('bus')


const uploading = ref(false)
const fileList = ref([])

const handlePreviewUpload = (e) => {
  openURL(e.url)
}

const handleChangeFileUpload = (uploadFile) => {
  uploading.value = true
  fileList.value = []
  sliceUploadFile(uploadFile.raw, 'hall-logo').then(data => {
    form['hallLogo'] = data['Key']
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

const formRef = ref()
const baseSelector = ref([])

bus.on('update-base-selector', () => {
  scienceBaseSelector().then(res => {
    baseSelector.value = res.data
  })
})
scienceBaseSelector().then(res => {
  baseSelector.value = res.data
})

const addNewBase = () => {
  bus.emit('edit-item', {
    record: {},
    component: BaseFormVue,
    title: '新增基地',
    name: Math.random()
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
  placeholder: '请输入展厅介绍',
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
const form = reactive({
  'hallName': '',
  'hallLogo': '',
  'baseId': '',
  'descSimple': '',
  'descRichText': '',
})

const rules = reactive({
  'hallName': [
    {
      required: true,
      trigger: 'blur',
      message: '请输入展厅名称'
    },
  ],
  'hallLogo': [
    {
      required: true,
      trigger: 'blur',
      message: '请上传展厅Logo'
    },
  ],
  'baseId': [
    {
      required: true,
      trigger: 'blur',
      message: '请选择所属基地'
    },
  ],
  'descSimple': [
    {
      required: true,
      trigger: 'blur',
      message: '请输入展厅简介'
    },
  ],
  'descRichText': [
    {
      required: true,
      trigger: 'blur',
      message: '请输入展厅内容'
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
  formEl.validate(async (valid) => {
    if (valid) {
      if (props.data && JSON.stringify(props.data) !== '{}') {
        await putBaseHall(form).then(res => {
          ElMessage({
            type: 'success',
            message: res.data,
          })
          onResetForm(formEl)
        })
      } else {
        await postBaseHall(form).then(res => {
          ElMessage({
            type: 'success',
            message: res.data,
          })
          onResetForm(formEl)
        })
      }
      valueHtml4Desc.value = ""
      fileList.value = []
      bus.emit('update-hall-table')
    }
  })
}

const props = defineProps({
  data: {}
})
if (props.data && JSON.stringify(props.data) !== '{}') {
  form['status'] = status.value.find(item => item.value === props.data['status']).key
  form['hallName'] = props.data['hallName']
  form['hallLogo'] = props.data['hallLogo']
  form['baseId'] = props.data['baseId']
  form['descSimple'] = props.data['descSimple']
  form['descRichText'] = props.data['descRichText']
  valueHtml4Desc.value = props.data['descRichText']
  form['id'] = props.data['id']
  fileList.value = [{
    name: '',
    url: props.data['hallLogoUrl']
  }]

}

</script>
