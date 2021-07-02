<?php
$widget_title = wp_kses_post($instance['widget_title']);
$btn_text = wp_kses_post($instance['btn_text']);
$btn_url = wp_kses_post($instance['btn_url']);
$icon_size = wp_kses_post($instance['icon_section']['icon_size']);
$icon_img_width = wp_kses_post($instance['icon_image_section']['icon_img_width']);
$icon_img_height = wp_kses_post($instance['icon_image_section']['icon_img_height']);
$icon_selection = wp_kses_post($instance['icon_selection']);
$selection = wp_kses_post($instance['opt_selector']);
$icon = siteorigin_widget_get_icon($instance['icon_section']['icon']);
$icon_image = wp_get_attachment_image_src($instance['icon_image_section']['icon_image'],'widgets_image_size');
$title = wp_kses_post($instance['title']);
$content = wp_kses_post($instance['content']);
$shape = wp_kses_post($instance['styling']['shapes']);

$box_top_border = wp_kses_post($instance['styling']['box_top_border']);
$box_right_border = wp_kses_post($instance['styling']['box_right_border']);
$box_bottom_border = wp_kses_post($instance['styling']['box_bottom_border']);
$box_left_border = wp_kses_post($instance['styling']['box_left_border']);
?>

<?php if ($widget_title) { ?>
    <h3 class="widget-title">
        <span><?php echo $widget_title ?></span>
    </h3>
<?php } ?>


<?php if ($icon_selection == 'top') { ?>
    <div class="service_top clearfix <?php echo $icon_selection; ?><?php if($box_top_border){ echo " top_b "; } if($box_right_border){ echo " right_b "; } if($box_bottom_border){ echo " bottom_b "; } if($box_left_border){ echo " left_b "; } ?>">
        <?php if($selection == 'icon'){ ?>
            <div class="icon <?php echo $shape ?>" style="font-size: <?php echo $icon_size; ?>px">
                <?php echo $icon; ?>
            </div>
        <?php } elseif($selection == 'icon_image'){ ?>
            <div class="icon_image">
                <img src=" <?php echo $icon_image[0]; ?>" alt="" style="width: <?php echo $icon_img_width ?>px;"/>
            </div>
        <?php } ?>
        <div class="content">
            <h3><?php echo $title; ?></h3>
            <?php echo $content; ?>
            <div class="clearfix"></div>
            <?php if($btn_text): ?>
                <a class="btn_style clearfix" href="<?php echo $btn_url; ?>" role="button" <?php if(wp_kses_post($instance['styling']['button_bg_color'])): ?> style="padding: 5px 20px;"<?php endif; ?>><?php echo $btn_text; ?></a>
            <?php endif; ?>
        </div>
    </div>

<?php } elseif ($icon_selection == 'left') { ?>
    <div class="service_left clearfix <?php echo $icon_selection ?><?php if($box_top_border){ echo " top_b "; } if($box_right_border){ echo " right_b "; } if($box_bottom_border){ echo " bottom_b "; } if($box_left_border){ echo " left_b "; } ?>">
        <?php if($selection == 'icon'){ ?>
            <div class="icon" style="font-size: <?php echo $icon_size; ?>px">
                <?php echo $icon; ?>
            </div>
        <?php } elseif($selection == 'icon_image'){ ?>
            <div class="icon_image">
                <img src=" <?php echo $icon_image[0]; ?>" alt="" style="width: <?php echo $icon_img_width ?>px;"/>
            </div>
        <?php } ?>
        <div class="content">
            <h3><?php echo $title; ?></h3>
            <?php echo $content; ?>
            <div class="clearfix"></div>
            <?php if($btn_text): ?>
                <a class="btn_style clearfix" href="<?php echo $btn_url; ?>" role="button" <?php if(wp_kses_post($instance['styling']['button_bg_color'])): ?> style="padding: 5px 20px;"<?php endif; ?>><?php echo $btn_text; ?></a>
            <?php endif; ?>
        </div>
    </div>

<?php }  elseif ($icon_selection == 'right') {?>


 <div class="service_right clearfix <?php echo $icon_selection ?><?php if($box_top_border){ echo " top_b "; } if($box_right_border){ echo " right_b "; } if($box_bottom_border){ echo " bottom_b "; } if($box_left_border){ echo " left_b "; } ?>">
        <div class="content">
            <h3><?php echo $title; ?></h3>
            <?php echo $content; ?>
            <div class="clearfix"></div>
            <?php if($btn_text): ?>
                <a class="btn_style clearfix" href="<?php echo $btn_url; ?>" role="button" <?php if(wp_kses_post($instance['styling']['button_bg_color'])): ?> style="padding: 5px 20px;"<?php endif; ?>><?php echo $btn_text; ?></a>
            <?php endif; ?>
        </div>

        <?php if($selection == 'icon'){ ?>
            <div class="icon" style="font-size: <?php echo $icon_size; ?>px">
                <?php echo $icon; ?>
            </div>
        <?php } elseif($selection == 'icon_image'){ ?>
            <div class="icon_image">
                <img src=" <?php echo $icon_image[0]; ?>" alt="" style="width: <?php echo $icon_img_width ?>px;"/>
            </div>
        <?php } ?>

</div>



<?php } ?>