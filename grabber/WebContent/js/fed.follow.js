/* Build by ued.5173.com 
 Date:2014-10-27 11:29:19 
 Version:1.00 */
 (function(a){a.fn.fedFollow=function(h,c){var b={left:0,top:0};if(c!=null)b=a.extend(b,c);var d=a(h),e=a(this),g=function(){var f=d.offset(),i=d.height();e.show().css({position:"absolute",left:f.left+b.left,top:f.top+i+b.top})};g();a(window).resize(function(){e.css("display")!="none"&&window.setTimeout(function(){g()},100)});return this}})(jQuery);
