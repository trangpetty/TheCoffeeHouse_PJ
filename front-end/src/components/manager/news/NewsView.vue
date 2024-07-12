<template>
  <div class="container-lg container-fluid p-4 box-shadow rounded mt-5" v-loading="ui.loading">
    <p class="cursor-pointer text-decoration-underline text-primary" @click="goBack">
      <font-awesome-icon icon="fa-solid fa-chevron-left" class="me-2" />
      <span>Back</span>
    </p>
    <input type="file" ref="fileInput" style="display: none" @change="handleFileChange">
    <el-form @submit.prevent="submitNews" :model="formData">
      <el-row>
        <el-col :span="6" class="me-3">
          <el-form-item label="Type">
            <el-select v-model="formData.type">
              <el-option label="#CoffeeLover" value="#CoffeeLover"/>
              <el-option label="Cập nhật từ Nhà" value="Cập nhật từ Nhà"/>
              <el-option label="Ưu đãi đặc biệt" value="Ưu đãi đặc biệt"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="Image">
            <el-upload action="#" list-type="picture-card" :auto-upload="false" :file-list="fileList" :on-change="handleUploadChange" :limit="1">
              <template v-if="fileList.length < 1" #trigger>
                <el-icon><Plus /></el-icon>
              </template>
              <template #file="{ file }">
                <div>
                  <img class="el-upload-list__item-thumbnail" :src="file.url" alt="" />
                  <span class="el-upload-list__item-actions">
                    <span class="el-upload-list__item-preview" @click="handlePictureCardPreview(file)">
                      <el-icon><ZoomIn /></el-icon>
                    </span>
                    <span class="el-upload-list__item-delete" @click="openFileInput(file)">
                      <el-icon><Edit /></el-icon>
                    </span>
                  </span>
                </div>
              </template>
            </el-upload>

            <el-dialog v-model="dialogVisible">
              <img :src="dialogImageUrl" alt="" width="100%" />
            </el-dialog>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item>
        <label for="">Title</label>
        <el-input type="text" id="title" v-model="formData.title" required />
      </el-form-item>
      <el-form-item label="Content" class="d-block">
        <div ref="editor"></div>
      </el-form-item>
      <el-button type="primary" @click="submitNews">Submit</el-button>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, defineProps, defineEmits } from 'vue';
import Quill from 'quill';
import { uploadFileToFirebaseAndGetURL } from "@/utils";
import axios from 'axios';
import {ElMessage} from "element-plus";
import {Edit, Plus, ZoomIn} from "@element-plus/icons-vue";

const props = defineProps({
  news: Object,
  visible: Boolean
});

const emit = defineEmits(['update:visible']);
const ui = ref({
  loading: false
})

const editor = ref<HTMLElement | null>(null);
let quillInstance: Quill | null = null;

const formData = ref({
  title: '',
  content: '',
  type: '',
  image: ''
});

const dialogImageUrl = ref('');
const dialogVisible = ref(false);
const fileList = ref([]);

const toolbarOptions = [
  ['bold', 'italic', 'underline', 'strike'],
  ['link', 'image'],
  [{ 'list': 'ordered'}, { 'list': 'bullet' }],
  [{ 'align': [] }],
  ['clean']
];

const modules = {
  toolbar: toolbarOptions,
};

const editorOptions = {
  modules,
  theme: 'snow'
};

onMounted(() => {
  if (editor.value) {
    quillInstance = new Quill(editor.value, editorOptions);

    quillInstance.on('text-change', () => {
      formData.value.content = quillInstance?.root.innerHTML;
    });

    quillInstance.getModule('toolbar').addHandler('image', () => {
      handleImageInsert();
    });

    // Load existing news data if editing
    if (props.news) {
      loadNewsData(props.news);
    }
  } else {
    console.error('Editor ref is not yet available');
  }
});

const loadNewsData = (news: any) => {
  formData.value.title = news.title;
  formData.value.content = news.content;
  formData.value.type = news.type;
  if(news.image) {
    formData.value.image = news.image;
    fileList.value = [{
      url: formData.value.image
    }]
  }
  quillInstance?.clipboard.dangerouslyPasteHTML(0, news.content);
};

const handleFileChange = async (event: Event) => {
  const file = (event.target as HTMLInputElement).files?.[0];
  if (!file) return;

  try {
    const downloadURL = await uploadFileToFirebaseAndGetURL(file, 'news');
    if (downloadURL) {
      const range = quillInstance?.getSelection(true);
      if (range) {
        const img = `<img src="${downloadURL}" alt="Uploaded Image">`;
        quillInstance?.clipboard.dangerouslyPasteHTML(range.index, img);
      }
    } else {
      console.error('Failed to upload file or get download URL');
    }
  } catch (error) {
    console.error('Error uploading file:', error);
  }
};

const handleUploadChange = (file, newFileList) => {
  fileList.value = newFileList.map(f => ({
    ...f,
    uid: f.uid || Date.now() // Ensure each file has a unique identifier
  }));
  console.log(fileList.value)
}

const handlePictureCardPreview = (file) => {
  dialogImageUrl.value = file.url;
  dialogVisible.value = true;
};

const openFileInput = (file) => {
  const input = document.createElement('input');
  input.type = 'file';
  input.accept = 'image/*';
  input.style.display = 'none';
  input.addEventListener('change', (event) => {
    handleFileInputChange(event, file);
  });
  document.body.appendChild(input);
  input.click();
  document.body.removeChild(input);
};

const handleFileInputChange = async (event, fileToEdit) => {
  const input = event.target;
  if (input.files && input.files.length > 0) {
    const newFile = input.files[0];
    const downloadURL = await uploadFileToFirebaseAndGetURL(newFile, 'news');
    formData.value.image = downloadURL;
    const fileIndex = fileList.value.findIndex(f => f.uid === fileToEdit.uid);
    if (fileIndex !== -1) {
      fileList.value[fileIndex] = { ...fileToEdit, raw: newFile, url: downloadURL };
    }
  }
};

const handleImageInsert = () => {
  const fileInput = document.querySelector('input[type="file"]');
  if (fileInput) {
    fileInput.click();
  }
};

const submitNews = async () => {
  try {
    ui.value.loading = true;
    for (const file of fileList.value) {
      try {
        formData.value.image = await uploadFileToFirebaseAndGetURL(file.raw, 'news');
      } catch (error) {
        console.error('Error uploading file:', error);
      }
    }
    if (props.news) {
      await axios.put(`http://10.30.100.178:8082/api/news/${props.news.id}`, formData.value);
      ElMessage({
        message: 'News updated successfully!',
        type: 'success',
      })
    } else {
      await axios.post('http://10.30.100.178:8082/api/news', formData.value);
      ElMessage({
        message: 'News submitted successfully!',
        type: 'success',
      })
    }
    emit('refreshData');
    setTimeout(() => {
      ui.value.loading =  false;
      emit('update:visible', false);
    }, 2000);
  } catch (error) {
    console.error('Error submitting news:', error);
    ElMessage({
      message: 'Failed to submit news. Please try again.',
      type: 'warning',
    })
  }
};

const goBack = () => {
  emit('update:visible', false);
};
</script>

<style scoped>
.form-news {
  width: 1000px;
}

::v-deep .el-form-item__content {
  display: block!important;
}
/* Add scoped styles if needed */
</style>
