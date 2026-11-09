/* -----------------------------------------------------------------------------
 * boost_container_map.i
 *
 * SWIG 4.5.1's Lib/java/std_map.i for boost::container::map, which libtorrent 2.1 uses for
 * entry::dictionary_type (libtorrent 2.0 used std::map). The Java proxy class extends
 * java.util.AbstractMap, like the std::map one, so string_entry_map keeps its Java API.
 * Only the namespace and the size fragment's name differ from std_map.i.
 * ----------------------------------------------------------------------------- */

%include <std_common.i>

// ------------------------------------------------------------------------
// boost::container::map
// ------------------------------------------------------------------------

%{
#include <boost/container/map.hpp>
#include <stdexcept>
%}

%fragment("SWIG_BoostMapSize", "header", fragment="SWIG_JavaIntFromSize_t") {
  SWIGINTERN jint SWIG_BoostMapSize(size_t size) {
    jint sz = SWIG_JavaIntFromSize_t(size);
    if (sz == -1) {
      throw std::out_of_range("map size is too large to fit into a Java int");
    }

    return sz;
  }
}

%javamethodmodifiers boost::container::map::sizeImpl "private";
%javamethodmodifiers boost::container::map::containsImpl "private";
%javamethodmodifiers boost::container::map::putUnchecked "private";
%javamethodmodifiers boost::container::map::removeUnchecked "private";
%javamethodmodifiers boost::container::map::find "private";
%javamethodmodifiers boost::container::map::begin "private";
%javamethodmodifiers boost::container::map::end "private";

%rename(Iterator) boost::container::map::iterator;
%nodefaultctor boost::container::map::iterator;
%javamethodmodifiers boost::container::map::iterator::getNextUnchecked "private";
%javamethodmodifiers boost::container::map::iterator::isNot "private";
%javamethodmodifiers boost::container::map::iterator::getKey "private";
%javamethodmodifiers boost::container::map::iterator::getValue "private";
%javamethodmodifiers boost::container::map::iterator::setValue "private";

namespace boost {
namespace container {

template<class K, class T, class C = std::less< K> > class map {

%typemap(javabase) boost::container::map< K, T, C >
    "java.util.AbstractMap<$typemap(jboxtype, K), $typemap(jboxtype, T)>"

%proxycode %{

  public int size() {
    return sizeImpl();
  }

  public boolean containsKey(java.lang.Object key) {
    if (!(key instanceof $typemap(jboxtype, K))) {
      return false;
    }

    return containsImpl(($typemap(jboxtype, K))key);
  }

  public $typemap(jboxtype, T) get(java.lang.Object key) {
    if (!(key instanceof $typemap(jboxtype, K))) {
      return null;
    }

    Iterator itr = find(($typemap(jboxtype, K)) key);
    if (itr.isNot(end())) {
      return itr.getValue();
    }

    return null;
  }

  public $typemap(jboxtype, T) put($typemap(jboxtype, K) key, $typemap(jboxtype, T) value) {
    Iterator itr = find(key);
    if (itr.isNot(end())) {
      $typemap(jboxtype, T) oldValue = itr.getValue();
      itr.setValue(value);
      return oldValue;
    } else {
      putUnchecked(key, value);
      return null;
    }
  }

  public $typemap(jboxtype, T) remove(java.lang.Object key) {
    if (!(key instanceof $typemap(jboxtype, K))) {
      return null;
    }

    Iterator itr = find(($typemap(jboxtype, K)) key);
    if (itr.isNot(end())) {
      $typemap(jboxtype, T) oldValue = itr.getValue();
      removeUnchecked(itr);
      return oldValue;
    } else {
      return null;
    }
  }

  public java.util.Set<Entry<$typemap(jboxtype, K), $typemap(jboxtype, T)>> entrySet() {
    java.util.Set<Entry<$typemap(jboxtype, K), $typemap(jboxtype, T)>> setToReturn =
        new java.util.HashSet<Entry<$typemap(jboxtype, K), $typemap(jboxtype, T)>>();

    Iterator itr = begin();
    final Iterator end = end();
    while (itr.isNot(end)) {
      setToReturn.add(new Entry<$typemap(jboxtype, K), $typemap(jboxtype, T)>() {
        private Iterator iterator;

        private Entry<$typemap(jboxtype, K), $typemap(jboxtype, T)> init(Iterator iterator) {
          this.iterator = iterator;
          return this;
        }

        public $typemap(jboxtype, K) getKey() {
          return iterator.getKey();
        }

        public $typemap(jboxtype, T) getValue() {
          return iterator.getValue();
        }

        public $typemap(jboxtype, T) setValue($typemap(jboxtype, T) newValue) {
          $typemap(jboxtype, T) oldValue = iterator.getValue();
          iterator.setValue(newValue);
          return oldValue;
        }
      }.init(itr));
      itr = itr.getNextUnchecked();
    }

    return setToReturn;
  }
%}

  public:
    typedef size_t size_type;
    typedef ptrdiff_t difference_type;
    typedef K key_type;
    typedef T mapped_type;
    typedef std::pair< const K, T > value_type;
    typedef value_type* pointer;
    typedef const value_type* const_pointer;
    typedef value_type& reference;
    typedef const value_type& const_reference;

    map();
    map(const map& other);

    struct iterator {
      %typemap(javaclassmodifiers) iterator "public class"
      %extend {
        boost::container::map< K, T, C >::iterator getNextUnchecked() {
          boost::container::map< K, T, C >::iterator copy = (*$self);
          return ++copy;
        }

        bool isNot(iterator other) const {
          return (*$self != other);
        }

        K getKey() const {
          return (*$self)->first;
        }

        T getValue() const {
          return (*$self)->second;
        }

        void setValue(const T& newValue) {
          (*$self)->second = newValue;
        }
      }
    };

    %rename(isEmpty) empty;
    bool empty() const;
    void clear();
    iterator find(const K& key);
    iterator begin();
    iterator end();
    %extend {
      %fragment("SWIG_BoostMapSize");

      jint sizeImpl() const throw (std::out_of_range) {
        return SWIG_BoostMapSize(self->size());
      }

      bool containsImpl(const K& key) {
        return (self->count(key) > 0);
      }

      void putUnchecked(const K& key, const T& value) {
%#ifdef __cpp_lib_map_try_emplace
        (*self).insert_or_assign(key, value);
%#else
        (*self)[key] = value;
%#endif
      }

      void removeUnchecked(const boost::container::map< K, T, C >::iterator itr) {
        self->erase(itr);
      }
    }
};

}
}
