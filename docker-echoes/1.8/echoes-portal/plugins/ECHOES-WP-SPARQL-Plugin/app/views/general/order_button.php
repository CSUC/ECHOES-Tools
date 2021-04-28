<script src="https://cdnjs.cloudflare.com/ajax/libs/semantic-ui/2.3.1/semantic.min.js" integrity="sha256-RKNmL9+6j/3jB72OcIg8OQr91Bi4OgFPnKQOFS1O+fo=" crossorigin="anonymous"></script>
<div style="width: 190px" class="avia-icon-pos-right small ui floating labeled icon dropdown button">
  <i class="sort icon"></i>
  <div class="text item"></div>
  <div class="menu">
  <?php foreach ($order_items as $label => $config): ?>
    <div class="item" data-value="<?php echo $config['data-value']; ?>">
      <i class="<?php echo $config['class']; ?>"></i>
      <?php echo $label; ?>
    </div>
  <?php endforeach; ?>
  </div>
</div>
<script>
    function search(){
        let order = $(".dropdown").dropdown('get value').split(" ");
        let order_field = order[0];
        let order_type = order[1];
        window.location.href = WP_SITEURL + '/<?php echo $order_entity; ?>/search/?s=' + $('#s').val() + '&page=1&order_type=' + order_type + '&order_field=' + order_field;
    }
    $(document).ready(function(){
        $(".dropdown").dropdown();      
        value = '<?php echo $order_data_value ?>';
        $(".dropdown").dropdown('set selected', value);
        $(".dropdown").dropdown({onChange: function(new_value) {
            if (new_value != value){
                search();
                value = new_value;
            }            
    }});
    });
</script>