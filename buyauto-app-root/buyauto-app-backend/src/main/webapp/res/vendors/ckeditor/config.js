/**
 * @license Copyright (c) 2003-2015, CKSource - Frederico Knabben. All rights
 *          reserved. For licensing, see LICENSE.md or
 *          http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function(config) {
	// Define changes to default configuration here. For example:
	// config.language = 'fr';
	// config.uiColor = '#AADC6E';
	config.height = 400;
	

	config.toolbarGroups = [
	                		{ name: 'document', groups: [ 'mode', 'document', 'doctools' ] },
	                		{ name: 'clipboard', groups: [ 'clipboard', 'undo' ] },
	                		{ name: 'editing', groups: [ 'find', 'selection', 'spellchecker', 'editing' ] },
	                		{ name: 'forms', groups: [ 'forms' ] },
	                		{ name: 'paragraph', groups: [ 'list', 'indent', 'blocks', 'align', 'bidi', 'paragraph' ] },
	                		{ name: 'insert', groups: [ 'insert' ] },
	                		{ name: 'colors', groups: [ 'colors' ] },
	                		{ name: 'links', groups: [ 'links' ] },
	                		{ name: 'basicstyles', groups: [ 'basicstyles', 'cleanup' ] },
	                		{ name: 'styles', groups: [ 'styles' ] },
	                		{ name: 'tools', groups: [ 'tools' ] },
	                		'/',
	                		'/',
	                		{ name: 'others', groups: [ 'others' ] },
	                		{ name: 'about', groups: [ 'about' ] }
	                	];

	                	config.removeButtons = 'Save,NewPage,Preview,Print,Templates,Replace,Find,SelectAll,Scayt,Form,Radio,Checkbox,TextField,Textarea,Select,Button,ImageButton,HiddenField,Subscript,Superscript,CreateDiv,Language,Anchor,Flash,PageBreak,Iframe,ShowBlocks,About,HorizontalRule,Smiley,SpecialChar,Blockquote,BidiRtl,BidiLtr,Copy,Cut';

};
