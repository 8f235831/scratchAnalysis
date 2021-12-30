$(document).ready(function () {
    // with plugin options
    $("#input-id").fileinput({
        uploadUrl: "/test",
        uploadAsync: false,
        language: 'zh',
    }).on('fileuploaded', function (event, previewId, index, fileId) {
        console.log('File Uploaded', 'ID: ' + fileId + ', Thumb ID: ' + previewId);
    }).on('fileuploaderror', function (event, data, msg) {
        console.log('File Upload Error', 'ID: ' + data.fileId + ', Thumb ID: ' + data.previewId);
    }).on('filebatchuploadcomplete', function (event, preview, config, tags, extraData) {
        console.log('File Batch Uploaded', preview, config, tags, extraData);
    });
});
